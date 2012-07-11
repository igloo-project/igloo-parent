#!/bin/bash

############################################################################################
#  Ce script permet de renommer les classes contenues dans un archetype Maven, afin de
#  les rendre indépendantes du projet duquel elles sont tirées
############################################################################################

for path in `find . -name '*BasicApplication*'`
do
	newPath=`echo ${path} | sed 's/BasicApplication/__appName__/'`;
	mv ${path} ${newPath}
done
