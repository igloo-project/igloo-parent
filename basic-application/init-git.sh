#!/bin/bash

export LANG=C
set -e

script_directory=$(dirname $(readlink -f $0))
artifact=$(basename ${script_directory})

project_name="$1"
git_url="$2"

if [ "${project_name}" == "" -o "${git_url}" == "" ]; then
	echo "Usage: $0 <project_name> <git_url>"
	exit 1
fi

if git ls-remote ${git_url} 2>&1 | grep -q "Could not read from remote repository"; then
	echo "The project ${project_name} does not exist. Please create it before executing this script."
	exit 1
fi

pushd ${script_directory} > /dev/null

sed -i 's/${projectName}/'${project_name}'/g' pom.xml
sed -i "s@    <module@\t\t<module@g" pom.xml
sed -i "s@  <module@\t<module@g" pom.xml
sed -i "s@  </module@\t</module@g" pom.xml

git init
git remote add origin ${git_url}
find . -mindepth 1 -maxdepth 1 -type d -exec git add {} \;
git add pom.xml
for file in `find . -name pom.xml`; do
	echo -e "target\ntarget2\n.classpath\n.project\n.settings"> $(dirname $file)/.gitignore
	git add $(dirname $file)/.gitignore
done

git commit -m "Initialisation projet" .

popd > /dev/null
