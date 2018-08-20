#! /bin/bash

ARTIFACTS=artifacts

if [ ! -d "${ARTIFACTS}" ]; then
  echo Artifact folder is missing, aborting...
  exit 1
fi

cp -ar artifacts/. .
