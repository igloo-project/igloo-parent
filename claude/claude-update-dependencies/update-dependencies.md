# Campagne de mise à jour des dépendances Maven

## Prérequis

Avant de lancer la campagne, tu as besoin de deux fichiers sources :

1. **Le fichier `output.md`** : contient le tableau des dépendances à mettre à jour avec les versions actuelles et cibles. Demande-moi le chemin de ce fichier.
2. **Le fichier `xxxx-xx-maj-historique.md`** : fichier d'historique des mises à jour à compléter avec les versions appliquées. Demande-moi le chemin de ce fichier.

## Étapes de la campagne

### 1. Lire le fichier `output.md`

- Lire le contenu du fichier `output.md` fourni par l'utilisateur.
- Ce fichier contient un tableau avec les colonnes : GroupId, Artifact, version actuelle, version cible, et des commentaires.
- Ne pas prendre en compte les commentaires sur ce qu'il faut mettre à jour ou non : l'utilisateur a déjà fait le tri.

### 2. Rechercher et mettre à jour les versions dans les pom.xml

Pour chaque dépendance ayant une version cible (colonne non vide) :

1. **Rechercher la version actuelle** textuellement dans tous les fichiers `pom.xml` du workspace via l'IDE (`mcp__idea__search_in_files_by_text` avec `fileMask=pom.xml` et `projectPath=/home/rfoucher/git/igloo-parent`). La recherche couvre les 3 projets liés : `igloo-maven`, `igloo-parent`, `igloo-commons`.
2. **Identifier le bon fichier et la bonne propriété** : les versions des dépendances sont généralement déclarées sous forme de propriétés Maven (`<igloo.xxx.version>`) dans :
   - `/home/rfoucher/git/igloo-maven/properties/pom.xml` (dépendances principales)
   - `/home/rfoucher/git/igloo-maven/pom.xml` (certains plugins et dépendances de build comme immutables)
   - `/home/rfoucher/git/igloo-maven/plugins-common/pom.xml` (plugins Maven)
   - `/home/rfoucher/git/igloo-parent/pom.xml` (quelques plugins dupliqués : site, owasp)
3. **Remplacer l'ancienne version par la nouvelle** dans le(s) fichier(s) concerné(s).
4. **Attention aux doublons** : certaines propriétés sont déclarées dans plusieurs pom.xml (indiqué par "Deux poms concernés" dans les notes). Il faut mettre à jour toutes les occurrences.

### 3. Mettre à jour le fichier d'historique

Dans le fichier `xxxx-xx-maj-historique.md` :

1. Pour chaque dépendance/plugin **mis à jour** : remplir la colonne "Ver. cible" avec la version en gras (ex: `**2.22.1**`).
2. Pour chaque dépendance/plugin **non mis à jour** (Ver. cible reste `-`) : mettre la version actuelle en gras dans la colonne "Ver. actuelle" (ex: `**5.1.0**`). Cela indique que la version actuelle est conservée telle quelle. **Utiliser une regex (ex: `sed`) pour traiter toutes les lignes d'un coup** plutôt que de le faire ligne par ligne. Exemple : `sed -i -E 's/\|( +)([0-9][^ *|]+)( +)\|( +)-( +)\|/| \1**\2**\3| \4-\5|/g' fichier.md`

### 4. Vérification finale

- Rechercher les anciennes versions dans le workspace pour s'assurer qu'aucune occurrence n'a été oubliée.
- Attention aux faux positifs : une même chaîne de version peut apparaître dans des contextes différents (ex: `1.21.0` peut être la version de `commons-codec` mais aussi de `gitflow-maven-plugin`).

## Notes importantes

- **Ne pas prendre de décisions sur ce qui est à mettre à jour ou non.** Par défaut, tout ce qui est dans la liste `output.md` doit être mis à jour : le tri est déjà fait en amont. En cas de doute, demander à l'utilisateur.
- Utiliser la recherche IDE (`mcp__idea__search_in_files_by_text`) pour chercher dans les 3 projets simultanément, pas d'agents Task/Explore.
- Ne jamais formater le code.
- Ne pas réorganiser les imports.
- Exécuter les agents un par un, jamais en parallèle.
