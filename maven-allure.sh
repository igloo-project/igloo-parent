#! /bin/bash

# do not interrupt on error
set +e

MAVEN_OPTS="$MAVEN_OPTS -Dallure.install.directory=$( pwd )/.allure"
echo mvn -Dallure.enabled=true -fae clean test site:site
mvn -Dallure.enabled=true -fae clean test site:site
TEST_RESULT=$?

# interrupt on error
set -e

# second site:site call to generate aggregated report
# compile step needed to ensure local artifacts are used
echo mvn -Dallure.enabled=true compile site:site
mvn -Dallure.enabled=true compile site:site

# prepare ssh communication
echo Load ssh key
eval $(ssh-agent -s)
echo "$SYNC_TEST_REPORTS_SSH_KEY" | tr -d '\r' | ssh-add - > /dev/null
mkdir -p ~/.ssh
chmod 700 ~/.ssh
echo "$SYNC_TEST_REPORTS_SSH_KNOWN_HOSTS" > ~/.ssh/known_hosts
chmod 644 ~/.ssh/known_hosts

SYNC_DATE_COMMIT="$( date +'%Y-%m-%dT%H:%m:%S' )@${CI_COMMIT_SHA:0:8}"
SYNC_FOLDER="${SYNC_TEST_REPORTS_DOCUMENT_ROOT}/${CI_PROJECT_NAME}/${CI_COMMIT_REF_NAME}/${SYNC_DATE_COMMIT}"
# rsync result
echo create \"${SYNC_FOLDER}\"
# warning: escaping pattern '"'$VAR'"' is important
ssh "${SYNC_TEST_REPORTS_USER}@${SYNC_TEST_REPORTS_HOST}" mkdir -p '"'${SYNC_FOLDER}'"'
# -s preserve space in remote folder name
rsync -s -az --chmod=g=rX,o= target/site/ "${SYNC_TEST_REPORTS_USER}@${SYNC_TEST_REPORTS_HOST}:${SYNC_FOLDER}"

exit $TEST_RESULT
