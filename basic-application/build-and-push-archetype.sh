#!/bin/bash

function usage() {
	echo "Usage: $0 <basic_application_directory> (local|snapshot|release)"
	exit 1
}

if [ $# -ne 2 ]; then
	usage
fi

basic_application_directory=$(readlink -f $1)
if [ "${basic_application_directory}" == "" ] || [ ! -d "${basic_application_directory}" ]; then
	usage
fi

pushd "${basic_application_directory}"

deploy_environment="$2"
case "${deploy_environment}" in
	"local")
		;;
	"snapshot")
		;;
	"release")
		;;
	*)
		usage
esac

temp_directory=$(mktemp --suffix=-archetype -d)

mkdir ${temp_directory}/basic_application

# copy the original directory to a temporary one
tar -c --exclude-vcs . | tar -x -C ${temp_directory}/basic_application

if [ $? -ne 0 ]; then
	echo 1>&2 "Error while copying the basic application project to a temporary directory."
	exit 1
fi

pushd "${temp_directory}/basic_application"

	# fix the version in the archetype.properties file
	version=$(grep -m 1 '<version>' pom.xml | sed -r 's/.*>(.*)<\/.*/\1/')
	sed -i "s/^archetype.version=.*$/archetype.version=${version}/" archetype.properties
	
	# remove files that are only needed to build the archetype
	rm README.md build-and-push-archetype.sh

	# actually build the archetype from the  project
	mvn -U -Pdevelopment -DskipTests=true clean package install archetype:create-from-project -Darchetype.properties=archetype.properties
	if [ $? -ne 0 ]; then
		echo 1>&2 "Error while building the archetype"
		exit 1
	fi
	mv target/generated-sources/archetype ${temp_directory}/

popd &> /dev/null

pushd ${temp_directory}/archetype &> /dev/null

	# last tweaks on archetype files
	for path in `find . -name '*BasicApplication*'`; do
		newPath=`echo ${path} | sed 's/BasicApplication/__archetypeApplicationNamePrefix__/'`;
		mv ${path} ${newPath}
	done

	# archetype deployment
	case "${deploy_environment}" in
		"local")
			mvn clean install
			;;
		"snapshot")
			mvn clean package deploy -DaltDeploymentRepository=nexus-kobalt-snapshots::default::https://nexus.tools.kobalt.fr/repository/kobalt-snapshots/
			;;
		"release")
			mvn clean package deploy -DaltDeploymentRepository=nexus-kobalt-releases::default::https://nexus.tools.kobalt.fr/repository/kobalt-releases/
			;;
	esac

popd &> /dev/null

popd &> /dev/null

echo
echo
echo "##################################################################"
echo "# Archetype published to ${deploy_environment}"
echo "#           built in ${temp_directory}"
echo "##################################################################"
