#! /bin/bash

# do not interrupt on error
set +e

MAVEN_OPTS="$MAVEN_OPTS -DskipTests=true -Dowasp.enabled=true -Dupdate-report.enabled=true"
export MAVEN_OPTS

# Use a faked version so we can install artifacts
# (if not installed, build ends with not found dependencies)
mvn versions:set -DnewVersion=DEPENDENCIES-SNAPSHOT -DprocessAllModules=true
mvn versions:commit

# fail at end needed (fail at end) as we want a complete overview
# do not fail at the first outdated / problematic dependency
echo mvn -fae -U clean install site:site '&&' mvn site:stage
mvn -fae -U clean install site:site && mvn site:stage
TEST_RESULT=$?

# interrupt on error
set -e

# prepare ssh communication
echo Load ssh key
eval $(ssh-agent -s)
echo "$SYNC_TEST_REPORTS_SSH_KEY" | tr -d '\r' | ssh-add - > /dev/null
mkdir -p ~/.ssh
chmod 700 ~/.ssh
echo "$SYNC_TEST_REPORTS_SSH_KNOWN_HOSTS" > ~/.ssh/known_hosts
chmod 644 ~/.ssh/known_hosts

SYNC_DATE_COMMIT="$( date +'%Y-%m-%dT%H:%m:%S' )@${CI_COMMIT_SHA:0:8}"
# -N: non-recursive. Site is aggregated in root project, we do not need to
#     perform stage-deploy in children modules.
mvn -N site:stage-deploy@stage-deploy \
	-DstagingSiteURL="scpexe://${SYNC_TEST_REPORTS_USER}@${SYNC_TEST_REPORTS_HOST}${SYNC_TEST_REPORTS_DOCUMENT_ROOT}/${CI_PROJECT_NAME}-dependencies/${CI_COMMIT_REF_NAME}/${SYNC_DATE_COMMIT}/"

if [ $TEST_RESULT -ne 0 ]; then
  echo "Dependency job ends with some test failures"
fi

exit $TEST_RESULT
