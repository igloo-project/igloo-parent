#! /bin/bash

# do not interrupt on error
set +e

MAVEN_OPTS="$MAVEN_OPTS -DskipTests -Dowasp.enabled=true -Dupdate-report.enabled=true"
export MAVEN_OPTS

echo mvn -U clean package site:site '&&' mvn site:stage
mvn -U clean package site:site && mvn site:stage
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

mvn stage:deploy -DstagingSiteUrl="scp://${SYNC_TEST_REPORTS_USER}@${SYNC_TEST_REPORTS_HOST}/${SYNC_TEST_REPORTS_DOCUMENT_ROOT}/${CI_PROJECT_NAME}-dependencies/${CI_COMMIT_REF_NAME}/${SYNC_DATE_COMMIT}"

if [ $TEST_RESULT -ne 0 ]; then
  echo "Dependency job ends with some test failures"
fi

exit $TEST_RESULT
