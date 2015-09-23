Instructions to build, deploy and exploit the Maven archetype
=============================================================

Build the archetype
-------------------

- to install the archetype locally:
```sh
./build-and-push-archetype.sh ../basic-application/ local
```

- to install the archetype on our repository:
```sh
./build-and-push-archetype.sh ../basic-application/ snapshot
```

Generate a new project
----------------------

using your local repository:
```sh
mvn archetype:generate -DarchetypeVersion=X.X -DarchetypeCatalog=local -DartifactId=your-artifact-id -DgroupId=your.group.id -Dversion=0.1-SNAPSHOT -Dpackage=com.your.package -DarchetypeApplicationNamePrefix="YourApplication" -DarchetypeFullApplicationName="Customer - Your application" -DarchetypeDatabasePrefix=c_database_prefix -DarchetypeDataDirectory=your-data-directory
```

using the snapshot repository:
```sh
mvn archetype:generate -DarchetypeCatalog=https://openwide:openwide@projects.openwide.fr/services/nexus/content/repositories/owsi-core-snapshots/ -DartifactId=your-artifact-id -DgroupId=your.group.id -Dversion=0.1-SNAPSHOT -Dpackage=com.your.package -DarchetypeApplicationNamePrefix="YourApplication" -DarchetypeFullApplicationName="Customer - Your application" -DarchetypeDatabasePrefix=c_database_prefix -DarchetypeDataDirectory=your-data-directory
```

using the release repository:
```sh
mvn archetype:generate -DarchetypeCatalog=https://openwide:openwide@projects.openwide.fr/services/nexus/content/repositories/owsi-core/ -DartifactId=your-artifact-id -DgroupId=your.group.id -Dversion=0.1-SNAPSHOT -Dpackage=com.your.package -DarchetypeApplicationNamePrefix="YourApplication" -DarchetypeFullApplicationName="Customer - Your application" -DarchetypeDatabasePrefix=c_database_prefix -DarchetypeDataDirectory=your-data-directory
```

Push the new project
--------------------

```sh
cd /tmp/
<run the command above to generate the project>
cd your-project
/bin/bash init-gitlab.sh <unix name of the Wombat project>
OR
/bin/bash init-svn.sh <unix name of the Wombat project>
```

Then get the project from Eclipse by using "checkout Maven project".

/!\ Don't use the project which is in the temp directory!
