#!/bin/bash

############################################################################################
#  Ce script permet de renommer les classes contenues dans un archetype Maven, afin de
#  les rendre indépendantes du projet duquel elles sont tirées
############################################################################################

# renommage des BasicApplication en __appName__

for path in `find . -name '*BasicApplication*'`; do
	newPath=`echo ${path} | sed 's/BasicApplication/__archetypeApplicationNamePrefix__/'`;
	mv ${path} ${newPath}
done

find . -name *.launch -exec sed -i 's/${archetypeDataDirectory}/${artifactId}/' {} \;
