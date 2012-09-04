*****************************************************************************
* Ce fichier est une aide à la génération d'un archetype Maven à partir du
* projet 'basic-application'.
* 
*****************************************************************************
1) Ouvrir une console et se placer dans le workspace contenant le projet parent 'basic-application'

2) Créer un export propre et non versionné du projet :
mkdir export
svn export basic-application export/basic-application

3) Lancer la création de l'archetype depuis le projet exporté :
cd export/basic-application
mvn archetype:create-from-project -Darchetype.properties=../../configuration/archetype.properties

4) Déplacer l'archetype ainsi généré dans un dossier à part : 
mv target/generated-sources/archetype ../..

5) Se placer dans le nouveau dossier de l'archetype et lancer script de renommage des classes :
cd ../../archetype
/bin/bash ../configuration/prepare-artifact.sh

6) Installer l'archetype créé :

pour déployer en local :
	* mvn clean install
	
pour déployer en snapshot :
	* mvn clean package deploy -DaltDeploymentRepository=nexus-owsi-core-snapshots::default::https://projects.openwide.fr/services/nexus/content/repositories/owsi-core-snapshots/

pour déployer en release :
	* mvn clean package deploy -DaltDeploymentRepository=nexus-owsi-core::default::https://projects.openwide.fr/services/nexus/content/repositories/owsi-core/

*************************************
* Exemple de génération de projet
*************************************

pour utiliser le repository local :
	* mvn archetype:generate -DarchetypeCatalog=local -DartifactId=hermes-referentiel-images -DgroupId=com.hermes -Dversion=0.1-SNAPSHOT -Dpackage=com.hermes.referentielimages -DarchetypeApplicationNamePrefix="ReferentielImages" -DarchetypeFullApplicationName="Hermès - Référentiel Images" -DarchetypeDatabasePrefix=h_referentiel_images -DarchetypeDataDirectory=referentiel-images

pour utiliser le repository snapshot :
	* mvn archetype:generate -DarchetypeCatalog=https://openwide:openwide@projects.openwide.fr/services/nexus/content/repositories/owsi-core-snapshots/ -DartifactId=hermes-referentiel-images -DgroupId=com.hermes -Dversion=0.1-SNAPSHOT -Dpackage=com.hermes.referentielimages -DarchetypeApplicationNamePrefix="ReferentielImages" -DarchetypeFullApplicationName="Hermès - Référentiel Images" -DarchetypeDatabasePrefix=h_referentiel_images -DarchetypeDataDirectory=referentiel-images

pour utiliser le repository release :
	* mvn archetype:generate -DarchetypeCatalog=https://openwide:openwide@projects.openwide.fr/services/nexus/content/repositories/owsi-core/ -DartifactId=hermes-referentiel-images -DgroupId=com.hermes -Dversion=0.1-SNAPSHOT -Dpackage=com.hermes.referentielimages -DarchetypeApplicationNamePrefix="ReferentielImages" -DarchetypeFullApplicationName="Hermès - Référentiel Images" -DarchetypeDatabasePrefix=h_referentiel_images -DarchetypeDataDirectory=referentiel-images