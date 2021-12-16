#! /usr/bin/python3

import os
import re
import shlex
import sys

# export MAVEN_OPTS to conditionally set -Dsonar.projectBranch option
# based on CI_COMMIT_REF_NAME value
# dev, master, mt-*: not set
# other branches: set to branch/tag name

branch = os.environ.get("CI_COMMIT_REF_NAME", None)
maven_opts = shlex.split(os.environ.get("MAVEN_OPTS", ""))

# check if branch is available
if not branch:
  print("# Fatal error; current branch information not available")
  sys.exit(1)

# check if branch needs a sonar custom setting
main_branches = ["dev", "master", "mt-*"]
if len([ i for i in main_branches if re.match(i, branch) ]) == 0:
  maven_opts.append("-Dsonar.projectBranch={branch}".format(branch=branch))

conf = """
export MAVEN_OPTS={maven_opts}
""".format(maven_opts=shlex.quote(' '.join([i for i in maven_opts])))

print(conf)
