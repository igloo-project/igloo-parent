*****************************************************************************
* Ce fichier est une aide à la génération d'un archetype Maven à partir du
* projet 'basic-application'.
* 
*****************************************************************************

- pour installer l'archetype en local :
./build-and-push-archetype.sh ../basic-application/ local

- pour installer l'archetype sur le repository OWSI :
./build-and-push-archetype.sh ../basic-application/ snapshot

*************************************
* Exemple de génération de projet
*************************************

pour utiliser le repository local :
	* mvn archetype:generate -DarchetypeCatalog=local -DartifactId=hermes-referentiel-images -DgroupId=com.hermes -Dversion=0.1-SNAPSHOT -Dpackage=com.hermes.referentielimages -DarchetypeApplicationNamePrefix="ReferentielImages" -DarchetypeFullApplicationName="Hermès - Référentiel Images" -DarchetypeDatabasePrefix=h_referentiel_images -DarchetypeDataDirectory=referentiel-images

pour utiliser le repository snapshot :
	* mvn archetype:generate -DarchetypeCatalog=https://openwide:openwide@projects.openwide.fr/services/nexus/content/repositories/owsi-core-snapshots/ -DartifactId=hermes-referentiel-images -DgroupId=com.hermes -Dversion=0.1-SNAPSHOT -Dpackage=com.hermes.referentielimages -DarchetypeApplicationNamePrefix="ReferentielImages" -DarchetypeFullApplicationName="Hermès - Référentiel Images" -DarchetypeDatabasePrefix=h_referentiel_images -DarchetypeDataDirectory=referentiel-images

pour utiliser le repository release :
	* mvn archetype:generate -DarchetypeCatalog=https://openwide:openwide@projects.openwide.fr/services/nexus/content/repositories/owsi-core/ -DartifactId=hermes-referentiel-images -DgroupId=com.hermes -Dversion=0.1-SNAPSHOT -Dpackage=com.hermes.referentielimages -DarchetypeApplicationNamePrefix="ReferentielImages" -DarchetypeFullApplicationName="Hermès - Référentiel Images" -DarchetypeDatabasePrefix=h_referentiel_images -DarchetypeDataDirectory=referentiel-images