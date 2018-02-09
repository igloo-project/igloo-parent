#! /bin/bash

# this file generate a file that can be used to override test configurations

if ! which shuf; then
  echo "Fatal error: missing shuf command"
  exit 1
fi

CONFIGURATION_FILE="gitlab-ci.build.conf"

BASE_PORT=$( shuf -i 30000-40000 -n 1 )
TEMP_DIR=$( mktemp -d --suffix -igloo )

cat <<EOF > "${CONFIGURATION_FILE}"
# generated on $( date )

export TEST_DB_TYPE=h2
export TEST_DB_NAME=igloo_test
export TEST_DB_USER=igloo_test
export TEST_DB_PASSWORD=igloo_test
export TEST_JERSEY_MOCK_HTTP_PORT=${BASE_PORT}
export TEST_INDEX_PATH=${TEMP_DIR}
export TEST_INFINISPAN_JGROUPS_PING_PORT=$(( ${BASE_PORT} + 1 ))
export TEST_INFINISPAN_JGROUPS_PORT_PORT=$(( ${BASE_PORT} + 2 ))

EOF

echo "Generated configuration"
echo "======================="
cat "${CONFIGURATION_FILE}"
echo "======================="
