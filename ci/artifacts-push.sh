#! /bin/bash

ARTIFACTS=artifacts

mkdir -p "${ARTIFACTS}"

for result in surefire-reports history jacoco.exec; do
  # skip artifacs folder and allure-results/history folders
  find '(' -name "$result" -a '!' -wholename "./""${ARTIFACTS}""/*" -a '!' -wholename "*/allure-results/*" ')' \
       -exec cp -ar --parents {} "${ARTIFACTS}/" \;
done
