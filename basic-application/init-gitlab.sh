#!/bin/bash

export LANG=C
set -e

script_directory=$(dirname $(readlink -f $0))
artifact=$(basename ${script_directory})

wombat_project_name=$1
git_root_url=git@git.projects.openwide.fr:open-wide/${wombat_project_name}.git

if [ "${wombat_project_name}" == "" ]; then
	echo "Usage: $0 <wombat_project_name>"
	exit 1
fi

if git ls-remote ${git_root_url} 2>&1 | grep -q "Could not read from remote repository"; then
	echo "The project ${wombat_project_name} does not exist or it does not include a GitLab project. Please create it before executing this script."
	exit 1
fi

pushd ${script_directory} > /dev/null

sed -i 's/${wombatProjectName}/'${wombat_project_name}'/g' pom.xml
sed -i "s@    <module@\t\t<module@g" pom.xml
sed -i "s@  <module@\t<module@g" pom.xml
sed -i "s@  </module@\t</module@g" pom.xml

git init
git remote add origin ${git_root_url}
find . -mindepth 1 -maxdepth 1 -type d -exec git add {} \;
git add pom.xml
for file in `find . -name pom.xml`; do
	echo -e "target\n.classpath\n.project\n.settings"> $(dirname $file)/.gitignore
	git add $(dirname $file)/.gitignore
done

git commit -m "Initialisation projet" .

popd > /dev/null
