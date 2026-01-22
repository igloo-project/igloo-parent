import asyncio
import os
import os.path
import pathlib
import tempfile
import time

import click
import requests
import termcolor

from igloo_release.command import Command, Commands
from igloo_release.process import command_header, run_stdout

FETCHES = ["main", "dev", "v5-main", "v5-dev"]
IGLOO_REPOSITORY = "https://nexus.tools.kobalt.fr/repository/igloo-releases/"


def home(project):
    return os.path.join(os.getenv("HOME"), "git", project)


@click.group("igloo-release")
def igloo_release():
    pass


@igloo_release.command("igloo")
@click.argument("release_version")
@click.argument("development_version")
@click.option("--igloo-maven-repository", default="git@github.com:igloo-project/igloo-maven")
@click.option("--igloo-commons-repository", default="git@github.com:igloo-project/igloo-commons")
@click.option("--igloo-parent-repository", default="git@github.com:igloo-project/igloo-parent")
@click.option("--igloo-maven-path", default=None)
@click.option("--igloo-commons-path", default=None)
@click.option("--igloo-parent-path", default=None)
@click.option("--release-branch", default=None)
@click.option("--skip-igloo-maven", is_flag=True, default=False)
@click.option("--skip-igloo-commons", is_flag=True, default=False)
@click.option("--igloo-5", is_flag=True, default=False)
@click.option("--push/--no-push", is_flag=True, default=False)
@click.option("--stdout/--no-stdout", is_flag=True, default=False)
@click.option("--dry-run", "-n", is_flag=True, default=False)
@click.option("--skip-snapshot", is_flag=True, default=False)
def igloo(
    release_version,
    development_version,
    igloo_maven_repository,
    igloo_commons_repository,
    igloo_parent_repository,
    igloo_maven_path,
    igloo_commons_path,
    igloo_parent_path,
    release_branch,
    skip_igloo_maven,
    skip_igloo_commons,
    igloo_5,
    push,
    stdout,
    dry_run,
    skip_snapshot,
):
    """\b
    RELEASE_VERSION is the expected new version.
    DEVELOPMENT_VERSION is the expected next development cycle version.
    \b
    When release is done:
    \b
    * A new tag for release is created.
    * branch `main` contains the released version.
    * branch `dev` contains the development version."""
    # TODO: do not create a temp folder
    if not release_branch:
        release_branch = "dev" if not igloo_5 else "v5-dev"
    temp_folder = tempfile.mkdtemp()
    if igloo_maven_path is None:
        igloo_maven_path = os.path.join(temp_folder, "igloo-maven")
    if igloo_commons_path is None:
        igloo_commons_path = os.path.join(temp_folder, "igloo-commons")
    if igloo_parent_path is None:
        igloo_parent_path = os.path.join(temp_folder, "igloo-parent")
    if not dry_run:
        print(termcolor.colored(f"# All logs are located in {temp_folder}.", "yellow"))
    else:
        pathlib.Path(temp_folder).rmdir()
        print(termcolor.colored("# This is a dry-run execution, no command is launched.", "yellow"))
    commands = Commands(temp_folder, stdout, dry_run)
    if not skip_igloo_maven:
        _release_igloo_maven(
            commands, release_version, development_version, igloo_maven_repository, igloo_maven_path, release_branch
        )
    else:
        print("igloo-maven: skipped")
    if not skip_igloo_commons:
        _release_igloo_commons(
            commands, release_version, development_version, igloo_commons_repository, igloo_commons_path, release_branch
        )
    else:
        print("igloo-commons: skipped")
    _release_igloo_parent(
        commands,
        release_version,
        development_version,
        igloo_parent_repository,
        igloo_parent_path,
        release_branch,
        igloo_5,
    )
    if not skip_snapshot:
        # switch back to snapshot versions
        _commons_prepare_versions(commands, development_version, igloo_commons_path)
        _parent_prepare_versions(commands, development_version, igloo_parent_path, igloo_5)
    if push:
        _push(
            commands,
            "igloo-maven",
            release_version,
            release_branch,
            "v5-main" if igloo_5 else "main",
            igloo_maven_path,
            push,
        )
        wait_for_artifact(
            commands,
            "igloo-maven",
            IGLOO_REPOSITORY,
            "org.iglooproject",
            "plugins-all",
            "pom",
            release_version,
            5 * 60,
            30,
        )
        _push(
            commands,
            "igloo-commons",
            release_version,
            release_branch,
            "v5-main" if igloo_5 else "main",
            igloo_commons_path,
            push,
        )
        wait_for_artifact(
            commands,
            "igloo-commons",
            IGLOO_REPOSITORY,
            "org.iglooproject.components",
            "igloo-security-api",
            "jar",
            release_version,
            10 * 60,
            30,
        )
        (
            _push(
                commands,
                "igloo-parent",
                release_version,
                release_branch,
                "v5-main" if igloo_5 else "main",
                igloo_parent_path,
                push,
            ),
        )
        if not igloo_5:
            _reset_owasp_branch(commands, "igloo-parent", "main", igloo_parent_path, push)
    _print_commands(commands)


def _print_commands(commands):
    """Display a command summary at script end. Either:

    - command success (no error)
    - command error (red) and remaining commands (yellow)
    - dry-run reminder if applicable"""
    if commands.dry_run:
        print()
        print(termcolor.colored("## No command was launched (dry-run)", "yellow"))
    elif not commands.failed:
        print()
        print(termcolor.colored("## Release performed successfully!", "green"))
    else:
        print()
        print(termcolor.colored("## Some commands failed, here is a report and the remaining commands", "red"))
        for command in commands.commands:
            if command.executed and not command.failed:
                pass
            elif command.failed:
                print(termcolor.colored("# This command failed", "red"))
                print(command_header(command, "red"))
                print(termcolor.colored("# ---------------------------------------", "yellow"))
                print(termcolor.colored("# Following commands were not launched", "yellow"))
                print(termcolor.colored("# ---------------------------------------", "yellow"))
                print()
            else:
                print(command_header(command, "yellow"))


@igloo_release.command("stdout", context_settings={"ignore_unknown_options": True})
@click.option("-o", "output", default="output.log")
@click.argument("command", nargs=-1, required=True)
def stdout(command, output):
    """Run a command, print result in terminal and in provided file. (debug purpose of tee implementation)"""
    asyncio.run(run_stdout("Command", command, output))
    asyncio.run(run_stdout("Command", command, output))


@igloo_release.command("wait-artifact")
@click.option("--repository", default=IGLOO_REPOSITORY)
@click.option("--type", default="jar")
@click.option("--wait-seconds", default="1800", type=click.INT)
@click.option("--interval-seconds", default="30", type=click.INT)
@click.argument("group_id")
@click.argument("artifact_id")
@click.argument("version")
def wait_artifact(repository, type, group_id, artifact_id, version, wait_seconds, interval_seconds):
    """Wait for artifact availability."""
    temp_folder = tempfile.mkdtemp()
    commands = Commands(temp_folder)
    wait_for_artifact(
        commands, "fake", repository, group_id, artifact_id, type, version, wait_seconds, interval_seconds
    )
    _print_commands(commands)


def _release_igloo_maven(
    commands, release_version, development_version, igloo_maven_repository, igloo_maven_path, release_branch
):
    """Perform igloo-maven release. Steps: repository, release-start, release-finish."""
    _prepare_repository(
        commands, "igloo-maven", igloo_maven_repository, igloo_maven_path, release_branch, FETCHES
    )
    _release_start_version(commands, "igloo-maven", release_version, development_version, igloo_maven_path)
    _release_finish_version(commands, "igloo-maven", release_version, development_version, igloo_maven_path)


def _release_igloo_commons(
    commands, release_version, development_version, igloo_commons_repository, igloo_commons_path, release_branch
):
    """Perform igloo-commons release. Steps: repository, version switches, release-start, release-finish."""
    _prepare_repository(
        commands, "igloo-commons", igloo_commons_repository, igloo_commons_path, release_branch, FETCHES
    )
    _release_start_version(commands, "igloo-commons", release_version, development_version, igloo_commons_path)
    _commons_prepare_versions(commands, release_version, igloo_commons_path)
    _release_finish_version(commands, "igloo-commons", release_version, development_version, igloo_commons_path)


def _release_igloo_parent(
    commands, release_version, development_version, igloo_parent_repository, igloo_parent_path, release_branch, igloo_5
):
    """Perform igloo-parent release. Steps: repository, version switches, release-start, release-finish."""
    _prepare_repository(
        commands, "igloo-parent", igloo_parent_repository, igloo_parent_path, release_branch, FETCHES
    )
    _release_start_version(commands, "igloo-parent", release_version, development_version, igloo_parent_path)
    _parent_prepare_versions(commands, release_version, igloo_parent_path, igloo_5)
    _release_finish_version(commands, "igloo-parent", release_version, development_version, igloo_parent_path)


def _commons_prepare_versions(commands, release_version, path):
    """igloo-commons version switches. Need to be done after release-start.

    - update parent version
    - update igloo.igloo-maven.version
    - commit changes"""
    check_call(
        commands,
        "igloo-commons-parent-update",
        f"igloo-commons: switch parent version to {release_version}",
        [
            "mvn",
            "-f",
            os.path.join(path, "pom.xml"),
            "versions:update-parent",
            "-pl",
            ".",
            f"-DparentVersion={release_version}",
            "-DskipResolution=true",
            "-DgenerateBackupPoms=false",
        ],
        cwd=path,
    )
    check_call(
        commands,
        "igloo-commons-igloo-maven-update",
        f"igloo-commons: switch igloo.igloo-maven.version to {release_version}",
        [
            "mvn",
            "-f",
            os.path.join(path, "pom.xml"),
            "versions:set-property",
            "-Dproperty=igloo.igloo-maven.version",
            f"-DnewVersion={release_version}",
            "-DprocessAllModules=true",
            "-DgenerateBackupPoms=false",
        ],
        cwd=path,
    )
    check_call(
        commands,
        "igloo-commons-dependencies-update-commit",
        "igloo-commons: commit version update",
        ["git", "-C", path, "commit", "-a", "-m", f"Update Igloo maven dependency to {release_version}"],
    )


def _parent_prepare_versions(commands, release_version, path, igloo_5):
    """igloo-parent version switches. Need to be done after release-start.

    - update parent version
    - update igloo-maven.version
    - update igloo-commons.version
    - commit changes"""
    """Nécessite d'être fait après le switch de version (release-start)."""
    projects = ".,:igloo-parent-maven-configuration-common"
    if not igloo_5:
        projects += ",:basic-application-app"
    check_call(
        commands,
        "igloo-parent-parent-update",
        f"igloo-parent: switch parent versions to {release_version}",
        [
            "mvn",
            "-f",
            os.path.join(path, "pom.xml"),
            "versions:update-parent",
            "-pl",
            projects,
            f"-DparentVersion={release_version}",
            "-DskipResolution=true",
            "-DgenerateBackupPoms=false",
        ],
        cwd=path,
    )
    check_call(
        commands,
        "igloo-parent-igloo-maven-update",
        f"igloo-parent: switch igloo-maven.version to {release_version}",
        [
            "mvn",
            "-f",
            os.path.join(path, "pom.xml"),
            "versions:set-property",
            "-Dproperty=igloo-maven.version",
            f"-DnewVersion={release_version}",
            "-DprocessAllModules=true",
            "-DgenerateBackupPoms=false",
        ],
        cwd=path,
    )
    check_call(
        commands,
        "igloo-parent-igloo-commons-update",
        f"igloo-parent: switch igloo-commons.version to {release_version}",
        [
            "mvn",
            "-f",
            os.path.join(path, "pom.xml"),
            "versions:set-property",
            "-Dproperty=igloo-commons.version",
            f"-DnewVersion={release_version}",
            "-DprocessAllModules=true",
            "-DgenerateBackupPoms=false",
        ],
        cwd=path,
    )
    check_call(
        commands,
        "igloo-parent-dependencies-update-commit",
        "igloo-parent: commit igloo-maven/commons version update",
        ["git", "-C", path, "commit", "-a", "-m", f"Update Igloo maven/commons dependency to {release_version}"],
    )


def _prepare_repository(commands, project, repository, path, branch, fetches):
    """Checkout repository and switch to appropriate branch."""
    if not os.path.exists(path):
        parent_path = pathlib.Path(path).parent
        if parent_path and not parent_path.exists():
            os.makedirs(parent_path)
        check_call(
            commands,
            f"{project}-git-clone",
            f"{project}: clone {repository} in {path}",
            ["git", "clone", "-q", "-b", branch, repository, path],
        )
    else:
        check_call(
            commands,
            f"{project}-git-switch",
            f"{project}: switch to branch {branch}",
            ["git", "-C", path, "checkout", branch],
        )
        check_call(
            commands,
            f"{project}-git-refresh-branch",
            f"{project}: refresh branch {branch}",
            ["git", "-C", path, "pull"],
        )
    check_call(
        commands,
        f"{project}-git-refresh-others",
        f"{project}: refresh main branches",
        ["git", "-C", path, "fetch", "-p", "-P", "origin", *[f"{i}:{i}" for i in fetches if i != branch]],
    )


def _release_start_version(commands, project, release_version, development_version, path):
    """Perform gitflow:release-start command."""
    check_call(
        commands,
        f"{project}-release-start",
        f"{project}: release-start {release_version}/{development_version}",
        [
            "mvn",
            "-f",
            os.path.join(path, "pom.xml"),
            "-B",
            "gitflow:release-start",
            f"-DreleaseVersion={release_version}",
            f"-DdevelopmentVersion={development_version}",
            "-DallowSnapshots=true",
        ],
        cwd=path,
    )


def _release_finish_version(commands, project, release_version, development_version, path):
    """Perform gitflow:release-finish command."""
    check_call(
        commands,
        f"{project}-release-finish",
        f"{project}: release-finish",
        [
            "mvn",
            "-f",
            os.path.join(path, "pom.xml"),
            "-B",
            "gitflow:release-finish",
            f"-DreleaseVersion={release_version}",
            f"-DdevelopmentVersion={development_version}",
        ],
        cwd=path,
    )


def _push(commands, project, release_version, branch, main_branch, path, push):
    """Perform a push operation if push is enabled. Else display a skip message."""
    if not push:
        print(f"# {project}: push skipped")
        return
    check_call(
        commands,
        f"{project}-push",
        f"{project}: push {branch}, {main_branch}, v{release_version}",
        ["git", "-C", path, "push", "origin", branch, main_branch, f"v{release_version}"],
    )


def _reset_owasp_branch(commands, project, main_branch, path, push):
    """Reset hard owasp branch from main."""
    check_call(
        commands,
        f"{project}owasp-reset",
        f"{project}: checkout owasp branch",
        ["git", "-C", path, "branch", "-f", "owasp", main_branch],
    )
    if not push:
        print(f"# {project}: reset owasp branch skipped")
        return
    check_call(
        commands,
        f"{project}owasp-push",
        f"{project}: push owasp branch",
        ["git", "-C", path, "push", "-f", "origin", "owasp"],
    )


def _switch_back_snapshot():
    """TODO Go back to snapshot versions."""


def check_call(commands, code, description, command_line, **kwargs):
    """Display and execute a command (if dry-run not enabled and if there is no previous error).
    If there is a previous error, only register command so that it can be displayed at script end a not-run command."""
    command = Command(
        description, command_line, os.path.join(commands.log_folder, f"{code}.log"), commands.stdout, commands.dry_run
    )
    if commands.failed:
        pass
    else:
        print(command_header(command, "green"))
        if not commands.dry_run:
            try:
                command.executed = True
                command.result = asyncio.run(run_stdout(command, **kwargs))
            except Exception as exc:  # noqa: BLE001
                print(termcolor.colored(f"Command failed with {exc}", "red"))
                command.result = 1
        else:
            command.result = 0
    commands.add_command(command)


def wait_for_artifact(
    commands, project, repository, group_id: str, artifact_id, type, version, max_wait_s, check_interval
):
    """Check if an artifact is available. Used to delay push once dependencies are available."""
    start = time.time()
    now = start
    url = repository
    if not url.endswith("/"):
        url = url + "/"
    url = url + group_id.replace(".", "/") + "/"
    url = url + artifact_id + "/"
    url = url + version + "/"
    url = url + artifact_id + "-" + version + "." + type
    command = Command(f"{project}: waiting for arfifact {url} for {max_wait_s} s.", None, None, False, commands.dry_run)
    if commands.failed:
        command.result = 0
    else:
        print(command_header(command, "green"))
        if not commands.dry_run:
            command.result = 1
            while now < start + max_wait_s:
                response = requests.head(url)
                if response.status_code == 200:
                    command.result = 0
                    break
                time.sleep(check_interval)
                now = time.time()
    commands.add_command(command)


if __name__ == "__main__":
    igloo_release()
