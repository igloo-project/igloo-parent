#!/bin/bash

function usage() {
	echo "Usage: $0 <basic_application_directory> (local|snapshot|release)"
	exit 1
}

script_directory=$(dirname $(readlink -f $0))

if [ $# -ne 2 ]; then
	usage
fi

basic_application_directory=$(readlink -f $1)
if [ "${basic_application_directory}" == "" ] || [ ! -d "${basic_application_directory}" ]; then
	usage
fi

pushd "${basic_application_directory}"
branch=$(git rev-parse --abbrev-ref HEAD)

deploy_environment="$2"
if [ "${deploy_environment}" != "local" ] && [ "${deploy_environment}" != "snapshot" ]  && [ "${deploy_environment}" != "release" ]; then
	usage
fi

temp_directory=$(mktemp --suffix=-archetype -d)

mkdir ${temp_directory}/basic_application

#git archive ${branch} | tar -x -C ${temp_directory}/basic_application
tar -c --exclude-vcs . | tar -x -C ${temp_directory}/basic_application

if [ $? -ne 0 ]; then
	exit 1
fi

pushd "${temp_directory}/basic_application"

# fix the version in the archetype.properties file
version=$(grep -m 1 'version' pom.xml | sed -r 's/.*>(.*)<.*/\1/')
sed -i "s/^archetype.version=.*$/archetype.version=${version}/" archetype.properties

# construction de l'archetype à partir du projet
	mvn -U -Pdevelopment -DskipTests=true clean package install archetype:create-from-project -Darchetype.properties=${script_directory}/archetype.properties
	if [ $? -ne 0 ]; then
		echo "error while building the archetype"
		exit 1
	fi
	mv target/generated-sources/archetype ${temp_directory}/

popd &> /dev/null

pushd ${temp_directory}/archetype &> /dev/null

# derniers ajustements manuels sur l'archetype
	for path in `find . -name '*BasicApplication*'`; do
		newPath=`echo ${path} | sed 's/BasicApplication/__archetypeApplicationNamePrefix__/'`;
		mv ${path} ${newPath}
	done

	find . -name *.launch -exec sed -i 's/${archetypeDataDirectory}/${artifactId}/' {} \;

# déploiement de l'archetype
	if [ ${deploy_environment} == "local" ]; then
		mvn clean install
	elif [ ${deploy_environment} == "snapshot" ]; then
		mvn clean package deploy -DaltDeploymentRepository=nexus-owsi-core-snapshots::default::https://projects.openwide.fr/services/nexus/content/repositories/owsi-core-snapshots/
	elif [ ${deploy_environment} == "release" ]; then
		mvn clean package deploy -DaltDeploymentRepository=nexus-owsi-core::default::https://projects.openwide.fr/services/nexus/content/repositories/owsi-core/
	fi

popd &> /dev/null

popd &> /dev/null

echo
echo
echo "##################################################################"
echo "# Archetype published to ${deploy_environment}"
echo "#           built in ${temp_directory}"
echo "##################################################################"
