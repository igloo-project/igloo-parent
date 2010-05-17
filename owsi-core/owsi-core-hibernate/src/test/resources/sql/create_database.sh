## A lancer à la main pour initialisation au début du projet.
## Une fois la base créée, il n'y a normalement pas besoin de relancer le script.

createuser -S -D -R -W -U postgres hibernatetest

createdb -O hibernatetest -U postgres hibernatetest