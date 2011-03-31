La library MPXJ n'est pas encore disponible comme une dépendance Maven.

Il est donc nécessaire de créer une dépendance à partir du JAR pour l'utiliser dans un projet Maven : 

1) Télécharger la dernière version de MPXJ sur http://sourceforge.net/projects/mpxj/files/
2) Dézipper MPXJ
3) Récupérer la librairy mpxj.jar
4) Utiliser la commande Maven (en remplaçant le numéro de version !) :

	mvn install:install-file -Dfile=mpxj.jar -DgroupId=fr.openwide -DartifactId=mxpj -Dversion=<version> -Dpackaging=jar
	
	Cette commande installe la dépendance en locale.
	
5) Importer la dependance créée dans votre projet Maven.