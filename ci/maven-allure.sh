#! /bin/bash

# do not interrupt on error
set +e

#HISTORY_SRC_FOLDER=target/site/allure-maven-plugin/history
#HISTORY_TGT_FOLDER=target/allure-results/history
#for POM in $( find -name pom.xml ); do
#  MODULE="$( dirname "$POM" )"
#  mkdir -p "${MODULE}/${HISTORY_TGT_FOLDER}"
#  for HISTORY_FILE in history.json retry-trend.json history-trend.json duration-trend.json categories-trend.json; do
#    HISTORY_URL="https://reports.tools.kobalt.fr/${CI_PROJECT_NAME}/${CI_COMMIT_REF_NAME}/last/artifacts/${MODULE}/${HISTORY_SRC_FOLDER}/${HISTORY_FILE}"
#    # -L --location-trusted added to follow redirection
#    curl -L --location-trusted -s --basic --fail \
#      -u "$SYNC_TEST_REPORTS_HISTORY_AUTHORIZATION" \
#      -o "${MODULE}/${HISTORY_TGT_FOLDER}/${HISTORY_FILE}" \
#      "$HISTORY_URL"
#    if [ $? -ne 0 ]; then
#      echo "Download of $HISTORY_FILE ($HISTORY_URL) failed"
#    fi
#  done
#done
#
#MAVEN_OPTS="$MAVEN_OPTS -Dallure.enabled=true -Djacoco.enabled=true -Dallure.install.directory=$( pwd )/.allure"
#export MAVEN_OPTS
MAVEN_OPTS="$MAVEN_OPTS -Djacoco.enabled=true"
export MAVEN_OPTS

echo mvn -fae integration-test site:site
mvn -fae integration-test site:site
TEST_RESULT=$?

## interrupt on error
#set -e
#
## second site:site call to generate aggregated report
## compile step needed to ensure local artifacts are used
#find basic-application/ igloo/ -name '*-trend.json' -exec rm {} ';'
#echo mvn -DskipTests package site:site
#mvn -DskipTests package site:site
#
#./ci/artifacts-push.sh
#
## prepare ssh communication
#echo Load ssh key
#eval $(ssh-agent -s)
#echo "$SYNC_TEST_REPORTS_SSH_KEY" | tr -d '\r' | ssh-add - > /dev/null
#mkdir -p ~/.ssh
#chmod 700 ~/.ssh
#echo "$SYNC_TEST_REPORTS_SSH_KNOWN_HOSTS" > ~/.ssh/known_hosts
#chmod 644 ~/.ssh/known_hosts
#
#SYNC_DATE_COMMIT="$( date +'%Y-%m-%dT%H:%m:%S' )@${CI_COMMIT_SHA:0:8}"
#SYNC_FOLDER="${SYNC_TEST_REPORTS_DOCUMENT_ROOT}/${CI_PROJECT_NAME}/${CI_COMMIT_REF_NAME}/${SYNC_DATE_COMMIT}"
## rsync result
#echo create \"${SYNC_FOLDER}\"
## warning: escaping pattern '"'$VAR'"' is important
#ssh "${SYNC_TEST_REPORTS_USER}@${SYNC_TEST_REPORTS_HOST}" mkdir -p '"'${SYNC_FOLDER}'"'
## -s preserve space in remote folder name
#rsync -s -az --chmod=g=rX,o= target/site/ "${SYNC_TEST_REPORTS_USER}@${SYNC_TEST_REPORTS_HOST}:${SYNC_FOLDER}"
#rsync -s -az --chmod=g=rX,o= artifacts "${SYNC_TEST_REPORTS_USER}@${SYNC_TEST_REPORTS_HOST}:${SYNC_FOLDER}"
## build a link to the last item
#ssh "${SYNC_TEST_REPORTS_USER}@${SYNC_TEST_REPORTS_HOST}" ln -T -sf "${SYNC_DATE_COMMIT}" "$( dirname "${SYNC_FOLDER}" )/last"

if [ $TEST_RESULT -ne 0 ]; then
  echo "Test job ends with some test failures"
fi

exit $TEST_RESULT
