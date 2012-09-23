#!/bin/bash

export LANG=C

script_directory=$(dirname $(readlink -f $0))
artifact=$(basename ${script_directory})

wombat_project_name=$1
svn_root_url=svn+ssh://svn.projects.openwide.fr/svnroot/${wombat_project_name}
svn_trunk_url=${svn_root_url}/trunk

if [ "${wombat_project_name}" == "" ]; then
	echo "Usage: $0 <wombat_project_name>"
	exit 1
fi

if svn list ${svn_root_url} 2>&1 | grep -q "No repository found"; then
	echo "The project ${wombat_project_name} does not exist. Please create it before executing this script."
	exit 1
fi

if ! svn list ${svn_root_url} | grep -q trunk; then
	svn mkdir -q -m "Initialisation SVN" ${svn_root_url}/branches ${svn_root_url}/tags ${svn_trunk_url}
fi

if svn list ${svn_trunk_url}/${artifact} 2>&1 | grep -q "non-existent"; then
	svn mkdir -q -m "Initialisation projet" ${svn_trunk_url}/${artifact}
else
	echo "A directory with the name ${artifact} already exists in ${svn_trunk_url}"
	exit 1
fi

pushd ${script_directory} > /dev/null

sed -i 's/${wombatProjectName}/'${wombat_project_name}'/g' pom.xml
sed -i "s@    <module@\t\t<module@g" pom.xml
sed -i "s@  <module@\t<module@g" pom.xml
sed -i "s@  </module@\t</module@g" pom.xml

svn co -q ${svn_trunk_url}/${artifact} .
svn propset -q svn:ignore "target
.classpath
.project
.settings
init-*.sh" .

find . -maxdepth 1 ! -name init-*.sh ! -name .svn -exec svn add -q {} \;
svn propset -q svn:ignore "target
.classpath
.project
.settings" ${artifact}-*

svn ci -q -m "Initialisation projet" .

popd > /dev/null
