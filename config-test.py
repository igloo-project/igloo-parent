#! /bin/python
# -*- coding: utf-8 -*-

from __future__ import unicode_literals
from __future__ import print_function

from datetime import datetime
import os
import random
import re
import shlex
import sys
import tempfile

try:  # py3
  from shlex import quote
except ImportError:  # py2
  from pipes import quote

# where the config is written
configuration_file = "gitlab-ci.build.conf"
# base listen port; this base port is incremented
# to obtain each needed port
base_port = random.randrange(30000, 40000)
# tempdir
temp_dir = tempfile.mkdtemp(dir=os.getcwd(), suffix="-igloo")
# built branch
branch = os.environ.get("CI_COMMIT_REF_NAME", None)

# check if branch is available
if not branch:
  print("Fatal error; current branch information not available")
  sys.exit(1)

# check if branch needs a sonar custom setting
main_branches = ["dev", "master", "mt-.*"]
sonar_opts=[]
if len([ i for i in main_branches if re.match(i, branch) ]) == 0:
  sonar_opts.append("-Dsonar.projectKey=${{project.groupId}}:${{project.artifactId}}:{branch}".format(branch=branch))
  sonar_opts.append("-Dsonar.projectName=${{project.name}} ({branch})".format(branch=branch))

conf = """
# generated on {date}

declare -a SONAR_OPTS=({sonar_opts})
export TEST_DB_TYPE=h2
export TEST_DB_NAME=igloo_test
export TEST_DB_USER=igloo_test
export TEST_DB_PASSWORD=igloo_test
export TEST_JERSEY_MOCK_HTTP_PORT={http_port}
export TEST_INDEX_PATH={temp_dir}
export TEST_INFINISPAN_JGROUPS_PING_PORT={infinispan_tcp_port}
export TEST_INFINISPAN_JGROUPS_PORT_PORT={infinispan_ping_port}

""".format(
  date=datetime.now().isoformat(),
  sonar_opts=' '.join([quote(i) for i in sonar_opts]),
  temp_dir=temp_dir,
  http_port=base_port,
  infinispan_tcp_port=base_port + 1,
  infinispan_ping_port=base_port + 2
).strip()

# write and display config
with open(configuration_file, "w") as f:
  f.write(conf)

print("Generated configuration file")
print("============================")
print(conf)
print("============================")

sys.exit(0)
