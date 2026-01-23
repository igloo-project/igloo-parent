"""This script generates a new Igloo project from targetted basic-application.

Igloo branch can be configured by command-line argument.

\b
* ARTIFACT_ID: project's artifactId (e.g. basic-application)
* GROUP_ID: project's groupId (e.g. igloo)
* PACKAGE: project's base package (e.g. basicapp)
* VERSION: project's version (e.g. 1.0-SNAPSHOT). Use 'CURRENT_IGLOO_VERSION' if you want
  application to use igloo version
* ARCHETYPE_CAMEL_CASE_NAME: Camel case application name (e.g. BasicApplication)
* ARCHETYPE_BEAN_NAME: Spring bean name (e.g. basicApplication)
* ARCHETYPE_FULL_NAME: Application full name (e.g. BasicApplication)
* ARCHETYPE_UNDESCORE_NAME: Application name with underscore (e.g. basic_application)

If GIT_USER and GIT_PASSWORD are set, they are used for repository authentication.

= How does it work: =

basic-application version is altered to be distinct from igloo version, then maven-archetype-plugin
is used to generated an archetype from project. maven-archetype-plugin basically replaces some
well-known configured string (basic-application, basicapp, Basic Application).

Once archetype is generated, provided configuration (package, application name, ...) is used to
generate a new project. It is needed to fix some pom settings not handled by archetype plugin
(parent for ARTIFACT-app, package name in pom.xml)."""

import glob
import logging
import os
import pathlib
import re
import shutil
import subprocess
import tempfile
import typing

import click
import colorlog
import lxml.etree as ET  # noqa: N812

__version__ = "0.1.0"
logger = logging.getLogger("igloo")


def init_logger():
    """Configure colored logs."""
    handler = colorlog.StreamHandler()
    handler.setFormatter(colorlog.ColoredFormatter("%(log_color)s%(levelname)s:%(name)s:%(message)s"))
    logger.addHandler(handler)


# defaults for archetype.properties
FILTERED_EXTENSIONS = (
    "ftl,conf,java,xml,txt,groovy,cs,mdo,aj,jsp,gsp,vm,html,xhtml,properties,launch,.classpath,.project,sql,nfpm.yaml"
)
EXCLUDE_PATTERNS = "target,.classpath,.project,.settings/,.factorypath"

# placeholder version for archetype
OVERRIDE_VERSION = "__OVERRIDE_VERSION__"
# fake version to ease ${revision} replacement
FAKE_VERSION = "9.9.999-FAKE"
CAPTURE_STDOUT = True


class ArchetypeDefinition:
    """Bean holding values to generate archetype.properties, both for archetype and project generation."""

    def __init__(
        self,
        artifact_id,
        group_id,
        package,
        version,
        archetype_camel_case_name,
        archetype_bean_name,
        archetype_full_name,
        archetype_underscore_name,
        filtered_extensions=FILTERED_EXTENSIONS,
        exclude_patterns=EXCLUDE_PATTERNS,
    ):
        self.artifact_id = artifact_id
        self.group_id = group_id
        self.package = package
        self.version = version
        self.archetype_camel_case_name = archetype_camel_case_name
        self.archetype_bean_name = archetype_bean_name
        self.archetype_full_name = archetype_full_name
        self.archetype_underscore_name = archetype_underscore_name
        self.filtered_extensions = filtered_extensions
        self.exclude_patterns = exclude_patterns

    def to_properties(self) -> typing.Mapping[str, str]:
        """Dictionary used to generate archetype.properties file."""
        return {
            "artifactId": self.artifact_id,
            "groupId": self.group_id,
            "package": self.package,
            "version": self.version,
            "packageName": self.package,
            "archetypeCamelCaseName": self.archetype_camel_case_name,
            "archetypeBeanName": self.archetype_bean_name,
            "archetypeFullName": self.archetype_full_name,
            "archetypeUnderscoreName": self.archetype_underscore_name,
            "archetype.filteredExtensions": self.filtered_extensions,
            "excludePatterns": self.exclude_patterns,
        }


@click.command("igloo-project-init", help=__doc__)
@click.argument("artifact_id")
@click.argument("group_id")
@click.argument("version")
@click.argument("package")
@click.argument("archetype_camel_case_name")
@click.argument("archetype_bean_name")
@click.argument("archetype_full_name")
@click.argument("archetype_underscore_name")
@click.option("--target-url", help="Optional target remote repository (e.g https://host.tld/group/repository)")
@click.option("--target-branch", default="main", help="Target remote branch")
@click.option("--dry-run", "-n", type=click.BOOL, default=False, help="Dry run, do not push project", is_flag=True)
@click.option("--igloo-url", default="https://github.com/igloo-project/igloo-parent", help="Igloo repository URL")
@click.option("--igloo-branch", help="Igloo repository branch")
@click.option("--target-username", default=None)
@click.option("--target-email", default=None)
@click.option("--debug", type=click.BOOL, default=False, help="Display command outputs", is_flag=True)
@click.option("--check-build", type=click.BOOL, default=False, help="Check that target project build", is_flag=True)
@click.option(
    "--check-test", type=click.BOOL, default=False, help="Check that target project build and test", is_flag=True
)
@click.option(
    "--check-diff",
    type=click.BOOL,
    default=False,
    help="Perform selective diff after generation (testing purpose)",
    is_flag=True,
)
@click.option(
    "--tmp-dir",
    default=pathlib.Path(tempfile.gettempdir()),
    type=click.Path(resolve_path=True, file_okay=False, path_type=pathlib.Path),
    help="Temporary folder",
)
@click.option(
    "--clean/--no-clean", default=True, help="--no-clean prevent any temporary file cleaning (for debug purpose)"
)
@click.option("--verbose", "-v", count=True, help="Verbose output (repeat option to increase verbosity)")
def main(
    artifact_id: str,
    group_id: str,
    package: str,
    version: str,
    archetype_camel_case_name: str,
    archetype_bean_name: str,
    archetype_full_name: str,
    archetype_underscore_name: str,
    target_url: str,
    target_branch: str,
    target_username: str,
    target_email: str,
    igloo_url: str,
    igloo_branch: str,
    tmp_dir: pathlib.Path,
    dry_run: bool,
    clean: bool,
    verbose: bool,
    debug: bool,
    check_diff: bool,
    check_build: bool,
    check_test: bool,
):
    if verbose >= 1:
        logger.setLevel(logging.DEBUG)
    elif verbose >= 0:
        logger.setLevel(logging.INFO)
    if debug:
        global CAPTURE_STDOUT  # noqa: PLW0603
        CAPTURE_STDOUT = False
    init_logger()
    if not tmp_dir.exists():
        logger.debug("Creating temporary directory %s", tmp_dir)
        os.makedirs(tmp_dir)
    if not clean:
        logger.debug("Temporary directory %s will not be cleaned after script.")
    if target_url:
        logger.debug("Project creation configured with remote %s, branch %s.", target_url, target_branch)
        if dry_run:
            logger.debug("Project creation configured with dry-run option. Project will not be effectively pushed.")
    else:
        logger.debug("Project creation configured without a target remote repository.")
    # Target definition
    definition = ArchetypeDefinition(
        artifact_id,
        group_id,
        package,
        # To ease version replacement, we need to use placeholders
        # archetype:create-from-project -> OVERRIDE_VERSION
        # archetype:generate -> FAKE_VERSION
        # pom fixes -> FAKE_VERSION -> ${revision}
        # igloo version: effective Igloo version
        FAKE_VERSION,
        archetype_camel_case_name,
        archetype_bean_name,
        archetype_full_name,
        archetype_underscore_name,
    )
    workdir = pathlib.Path(tempfile.mkdtemp(dir=tmp_dir, prefix="igloo-project-init-"))
    logger.info("Use %s as temporary directory.", workdir)
    try:
        # clone igloo branch
        igloo_clone = clone_igloo_project(igloo_url, igloo_branch, workdir)
        # retrieve current igloo version
        igloo_version = extract_version(workdir, igloo_clone)
        if version.lower() == "current_igloo_version":
            version = igloo_version
        # split versions
        # - basic-application -> ${revision} -> __OVERRIDE_VERSION__
        # - igloo -> ${revision} -> igloo_version
        # - parent -> ${revision} -> igloo_version / unset <relativePath>
        # - add igloo.version property + revision property
        override_versions(igloo_clone, igloo_version)
        # configure archetype generation ; we setup the placeholder version
        build_archetype_properties(igloo_clone)
        # build archetype ; as version must be different than igloo version, we use the placeholder version
        generate_archetype(igloo_clone)
        # generate project from just generated archetype
        project_path = generate_project(workdir, OVERRIDE_VERSION, definition)
        # fix pom.xml (things not handled by archetype plugin)
        fix_poms(project_path, version, definition.artifact_id, definition.package)
        format_pom(project_path)
        if check_build or check_test:
            if check_test:
                # test implies build
                check_build = check_test
            check(project_path, check_build, check_test)
        if target_url:
            setup_git(igloo_clone, project_path, target_url, target_branch, target_username, target_email, dry_run)
        if check_diff:
            perform_diff(igloo_clone, project_path)
    finally:
        if clean:
            shutil.rmtree(workdir)
            logger.debug("Cleaned temporary directory %s.", workdir)
    if not clean:
        logger.info("Temporary directory not cleaned.")


def check(project_path: pathlib.Path, check_build: bool, check_test: bool):
    """Check that generated project build and (conditionaly)it compass tests."""
    if not (check_build or check_test):
        raise click.exceptions.Abort("Either check_build or check_test must be True. Both are False.")
    logger.debug("Checking project build (build=%s, test=%s).", check_build, check_test)
    args = ["mvn", "clean", "package"]
    if not check_test:
        args.append("-DskipTests")
    subprocess.check_call(args, **subprocess_args(True), cwd=project_path)
    logger.info("Generated project successfully build (build=%s, test=%s).", check_build, check_test)


def perform_diff(igloo_clone: pathlib.Path, project_path: pathlib.Path):
    """Check that basic-application and project matches (project needs to be generated with the appropriate configuration)."""
    args = [
        "diff",
        "-Nru",
        "--ignore-all-space",
        "--exclude",
        "target",
        "--exclude",
        ".settings",
        "--exclude",
        ".project",
        "--exclude",
        ".classpath",
        "--exclude",
        ".factorypath",
        "--exclude",
        ".gitignore",
        "--exclude",
        "owasp-suppressions.xml",
        "--exclude",
        ".git",
        "--exclude",
        "archetype.properties",
        "--exclude",
        "maven.config",  # does not exist in orginal project
        "--exclude",
        "pom.xml",  # does not match because of version / module re-ordering / snapshot tag rewritten
        str(igloo_clone.joinpath("basic-application")),
        str(project_path),
    ]
    try:
        subprocess.check_call(args)
    except subprocess.CalledProcessError:
        logger.error("Basic-application and project does not match. See diff result above.")  # noqa: TRY400
        raise click.exceptions.Exit(1) from None
    else:
        logger.info("Diff performed and succeed between basic-application and project.")


def subprocess_args(also_stderr: bool = False) -> typing.Mapping:
    """Generate subprocess kwargs to selectively capture STDOUT/STDERR of commands. Captured output
    is not displayed."""
    result = {}
    if CAPTURE_STDOUT:
        result["stdout"] = subprocess.DEVNULL
        if also_stderr:
            result["stderr"] = subprocess.DEVNULL
    return result


def setup_git(
    igloo_path: pathlib.Path,
    project_path: pathlib.Path,
    target_remote_url: str,
    remote_branch: str,
    target_username: str,
    target_email: str,
    dry_run: bool,
):
    """Push project to remote repository. If `dry_run` is true, push is performed with `--dry-run` option."""
    if target_username:
        subprocess.check_call(["git", "config", "--global", "user.name", target_username])
    if target_email:
        subprocess.check_call(["git", "config", "--global", "user.email", target_email])
    logger.debug("Git project init (branch %s).", remote_branch)
    logger.debug("Added .gitignore file from igloo project.")
    shutil.copy(igloo_path.joinpath(".gitignore"), project_path.joinpath(".gitignore"))
    logger.debug("Added owasp-suppressions.xml file from igloo project.")
    shutil.copy(igloo_path.joinpath("owasp-suppressions.xml"), project_path.joinpath("owasp-suppressions.xml"))
    subprocess.check_call(["git", "init", "-b", remote_branch], cwd=project_path, **subprocess_args(True))
    if os.getenv("GIT_USER", None) and os.getenv("GIT_PASSWORD", None):
        logger.info("Setup GIT_USER and GIT_PASSWORD credentials.")
        subprocess.check_call(
            [
                "git",
                "config",
                "--local",
                "credential.helper",
                '!f() { echo "username=$GIT_USER"; echo "password=$GIT_PASSWORD"; }; f',
            ],
            cwd=project_path,
            **subprocess_args(True),
        )
    logger.debug("Add git remote %s.", target_remote_url)
    subprocess.check_call(
        ["git", "remote", "add", "origin", target_remote_url], cwd=project_path, **subprocess_args(True)
    )
    logger.debug("Fetch git remote %s.", target_remote_url)
    subprocess.check_call(["git", "fetch"], cwd=project_path, **subprocess_args(True))
    subprocess.check_call(["git", "add", "-A"], cwd=project_path, **subprocess_args(True))
    subprocess.check_call(["git", "commit", "-m", "Initial commit"], cwd=project_path, **subprocess_args(True))
    dry_run_args = []
    if dry_run:
        logger.info("Push option triggered with dry-run option.")
        dry_run_args.append("--dry-run")
    logger.debug("Push content to remote (dry-run=%s).", dry_run)
    subprocess.check_call(
        ["git", "push", *dry_run_args, "origin", remote_branch], cwd=project_path, **subprocess_args(True)
    )
    logger.info("Project pushed to %s, branch %s, dry-run=%s.", target_remote_url, remote_branch, dry_run)


def format_pom(project_path: pathlib.Path):
    """Reindent pom file. This is needed because of https://github.com/apache/maven-archetype/pull/35#issuecomment-1925768656
    and https://github.com/apache/maven-archetype/pull/128."""
    pom_file = project_path.joinpath("pom.xml")
    env = dict(os.environ)
    env["XMLLINT_INDENT"] = "\t"
    output = subprocess.check_output(["xmllint", "--format", str(pom_file)], env=env, encoding="utf-8")
    pom_file.write_text(output)
    logger.info("Root pom reformatted.")
    subprocess.check_call(["mvn", "spotless:apply"], cwd=project_path, **subprocess_args())
    logger.info("Spotless applied.")


def override_versions(igloo_clone: pathlib.Path, igloo_version: str):
    """${revision} -> __OVERRIDE_VERSION__ if basic-application
    ${revision} -> igloo_version if parent
    replace <relativePath>...</relativePath> with <relativePath />
    add igloo.version property
    add revision property
    """
    # replace <versionPlaceholderDoNotRemove /> line with igloo.version declaration
    pom_file = igloo_clone.joinpath("basic-application", "pom.xml")
    basicapp_pom = pom_file.read_text()
    igloo_version_sub = re.compile("^(.*)<versionPlaceholderDoNotRemove />.*$", re.MULTILINE)
    updated_pom = igloo_version_sub.sub(
        f"\\1<igloo.version>{igloo_version}</igloo.version>\n\t\t<revision>{OVERRIDE_VERSION}</revision>", basicapp_pom
    )
    pom_file.write_text(updated_pom)
    # update basicapp version
    subprocess.check_call(
        [
            "mvn",
            "versions:set",
            "-pl",
            "basic-application",
            "-DartifactId=basic-application*",
            f"-DnewVersion={OVERRIDE_VERSION}",
            "-DgenerateBackupPoms=false",
            "-DskipResolution=true",
        ],
        cwd=igloo_clone,
        **subprocess_args(),
    )

    fix_parent_pom(igloo_clone.joinpath("basic-application/pom.xml"))
    fix_parent_pom(igloo_clone.joinpath("basic-application/basic-application-app/pom.xml"))

    # replace remaining ${revision}
    replace_version(igloo_clone.joinpath("basic-application/pom.xml"), "version", "${revision}", igloo_version)
    replace_version(
        igloo_clone.joinpath("basic-application/basic-application-app/pom.xml"), "version", "${revision}", igloo_version
    )

    logger.debug("Basic application version overriden to split igloo and basic-application versions.")


def build_archetype_properties(igloo_clone: pathlib.Path):
    """Build archetype.properties ; this file declares values and files where patterns (basic-application,
    BasicApplication, ...) are replaced by placeholders."""
    archetype_properties = igloo_clone.joinpath("basic-application", "archetype.properties")
    definition = ArchetypeDefinition(
        "basic-application",
        "org.iglooproject.archetype",
        "basicapp",
        OVERRIDE_VERSION,
        "BasicApplication",
        "basicApplication",
        "Basic Application",
        "basic_application",
    )
    content = "\n".join([f"{k}={v}" for k, v in definition.to_properties().items()])
    pathlib.Path(archetype_properties).write_text(content)
    logger.debug("Updated archetype.properties content:\n%s", content)


def extract_version(workdir: pathlib.Path, igloo_clone: pathlib.Path) -> str:
    """Extract current igloo version."""
    with tempfile.NamedTemporaryFile(dir=workdir, prefix="version-") as output_file:
        subprocess.check_call(
            [
                "mvn",
                "help:evaluate",
                "-pl",
                "basic-application",
                "-Dexpression=project.version",
                f"-Doutput={output_file.name}",
            ],
            cwd=igloo_clone,
            **subprocess_args(),
        )
        version = pathlib.Path(output_file.name).read_text()
        logger.info("Igloo version: %s", version)
        return version


def generate_project(
    workdir: pathlib.Path,
    archetype_version: str,
    definition: ArchetypeDefinition,
) -> pathlib.Path:
    """Generate a new project with archetype:generate command. Created project still needs to be
    reprocessed to fix things not handled by maven-archetype-plugin."""
    project = workdir.joinpath(definition.artifact_id)
    # we user FAKE_VERSION as a placholder
    # pom fixes then replace FAKE_VERSION by ${revision}
    java_properties = {
        "interactiveMode": "false",
        "archetypeCatalog": "local",
        "archetypeGroupId": "org.iglooproject.archetype",
        "archetypeArtifactId": "basic-application-archetype",
        "archetypeVersion": archetype_version,
        "version": FAKE_VERSION,
    }
    java_properties.update(definition.to_properties())
    java_args = [f"-D{k}={v}" for k, v in java_properties.items()]
    subprocess.check_call(
        ["mvn", "archetype:generate", f"-DoutputDirectory={project}", *java_args], **subprocess_args()
    )
    project_path = project.joinpath(definition.artifact_id)
    logger.info("Project %s:%s generated into %s", definition.group_id, definition.artifact_id, project_path)
    # generated folder contains `artifact_id` folder
    return project_path


def fix_poms(project_path: pathlib.Path, version: str, artifact_id: str, package_name: str):
    """Fix things not handled correctly by archetype plugin:
    * setup ${revision} CI-friendly versions
    * package in pom.xml (used for spring application configuration and scss processing)
    * parent for ARTIFACT-app
    """
    fix_all_versions(project_path, version)
    fix_pom(project_path.joinpath(f"{artifact_id}-back/pom.xml"), package_name)
    fix_pom(project_path.joinpath(f"{artifact_id}-front/pom.xml"), package_name)
    fix_pom(project_path.joinpath(f"{artifact_id}-app/pom.xml"), package_name)
    # https://github.com/apache/maven-archetype/issues/983 workaround
    # internal dependencies are broken
    replace_xml(
        project_path.joinpath(f"{artifact_id}-front/pom.xml"),
        f".//pom:dependency/pom:artifactId[text()='{artifact_id}']",
        artifact_id + "-back",
    )
    replace_xml(
        project_path.joinpath(f"{artifact_id}-app/pom.xml"),
        f".//pom:dependency/pom:artifactId[text()='{artifact_id}']",
        artifact_id + "-front",
    )
    # end workaround
    logger.info("Pom fixes applied (indentation, parent relative path, app parent, packages).")


def fix_all_versions(root_path: pathlib.Path, version: str):
    """Setup pom version with ${revision}."""
    for pom in [root_path.joinpath(i) for i in glob.glob("**/pom.xml", root_dir=root_path, recursive=True)]:
        replace_version(pathlib.Path(pom), "version", FAKE_VERSION, "${revision}")
    replace_version(root_path.joinpath("pom.xml"), "revision", FAKE_VERSION, version)
    logger.debug("Version updated to ${revision} in poms")


def fix_parent_pom(root_pom_path: pathlib.Path):
    """Remove relativePath declaration."""
    content = root_pom_path.read_text()
    new_content = re.compile(r"<relativePath>[^<]+</relativePath>.*").sub("<relativePath />", content)
    root_pom_path.write_text(new_content)
    logger.debug("Root pom fixed (unset relativePath)")


def replace_version(pom_file: pathlib.Path, tag_name: str, from_version: str, to_version: str):
    """Substitute from_version by to_version (may be ${revision}) (not possible with versions-maven-plugin)."""
    content = pom_file.read_text()
    content = content.replace(f"<{tag_name}>{from_version}</{tag_name}>", f"<{tag_name}>{to_version}</{tag_name}>")
    pom_file.write_text(content)
    logger.debug("Version updated to ${revision} in %s", pom_file)


def fix_pom(pom_file: pathlib.Path, package_name: str):
    """Replace basicapp. with correct package."""
    content = pom_file.read_text()
    new_content = re.compile(r"basicapp\.").sub(package_name + ".", content)
    pom_file.write_text(new_content)
    logger.debug("Fix project poms (package names)")


def generate_archetype(igloo_clone: pathlib.Path) -> pathlib.Path:
    """Create archetype project with archetype:create-from-project command."""
    basic_application = igloo_clone.joinpath("basic-application")
    archetype = basic_application.joinpath("target", "generated-sources", "archetype")
    os.makedirs(archetype)
    env: dict[str, str] = {}
    env.update(os.environ)
    # if remote debugging is needed
    # env.update({"JDK_JAVA_OPTIONS": "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000"})
    subprocess.check_call(
        [
            "mvn",
            "-N",
            "-B",
            "archetype:create-from-project",
            "-Darchetype.properties=archetype.properties",
            "-Darchetype.preserveCData",
        ],
        cwd=basic_application,
        **subprocess_args(),
        env=env,
    )
    logger.info("Archetype generated into %s.", archetype)
    subprocess.check_call(["mvn", "clean", "install"], cwd=archetype, **subprocess_args())
    return archetype


def clone_igloo_project(igloo_git_url: str, igloo_branch: str, target_dir: pathlib.Path) -> pathlib.Path:
    """Clone igloo project on appropriate branch."""
    args = [
        "git",
        "-C",
        str(target_dir),
        "clone",
        "--depth",
        "1",
    ]
    if igloo_branch:
        args.extend(["-b", igloo_branch])
    args.extend([igloo_git_url, "igloo-parent"])
    subprocess.check_call(
        args,
        **subprocess_args(True),
    )
    logger.info(
        "Igloo project branch %s cloned into %s.",
        igloo_branch if igloo_branch else "default",
        os.path.join(target_dir, "igloo-parent"),
    )
    return target_dir.joinpath("igloo-parent")


def replace_xml(pom_file: pathlib.Path, xpath: str, value: str, fail_if_no_match: bool = True):
    tree = ET.parse(pom_file)
    items: typing.Any = tree.xpath(xpath, namespaces={"pom": "http://maven.apache.org/POM/4.0.0"})
    if not items and fail_if_no_match:
        raise Exception(f"No match found for {xpath} in {pom_file}")  # noqa: TRY002 EM102
    for item in items:
        item.text = value
    tree.write(pom_file)


if __name__ == "__main__":
    main()
