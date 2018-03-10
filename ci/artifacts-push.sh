#! /bin/bash

ARTIFACTS=artifacts

mkdir -p "${ARTIFACTS}"

for result in surefire-reports allure-results jacoco.exec; do
  find '(' -name "$result" -a '!' -wholename -a '!' -wholename "./""${ARTIFACTS}""/*" ')' \
       -exec cp -ar --parents {} "${ARTIFACTS}/" \;
done
