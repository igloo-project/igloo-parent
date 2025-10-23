#! /bin/bash
set -e

# exec plugin use project.basedir as CWD
mkdir -p target/

SHA="$1"
shift
VERSION="$1"
shift

if [[ "${VERSION}" =~ -SNAPSHOT$ ]]; then
    VERSION=999-$SHA
    echo overriding version with $VERSION
fi

VERSION="$VERSION" nfpm "${@}"
