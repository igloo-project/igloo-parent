*****************************************************************************
* Ce fichier est une aide à la génération d'un archetype Maven à partir du
* projet 'basic-application'.
* 
*****************************************************************************
1) Ouvrir une console et se placer dans le workspace contenant le projet parent 'basic-application'

2) Créer un export propre et non versionné du projet :
	* mkdir export
	* svn export basic-application export/basic-application

3) Lancer la création de l'archetype : 
	* mvn archetype:create-from-project -Darchetype.properties=../../configuration/archetype.properties

4) Déplacer l'archetype ainsi généré dans un dossier à part : 
	* mv target/generated-sources/archetype ../..

5) Se placer dans le nouveau dossier de l'archetype et lancer script de renommage des classes :
	* cd ../..
	* sh ../configuration/rename_archetype_classes.sh
