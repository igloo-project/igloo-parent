#!/usr/bin/env python3
"""Supprime d'un dépôt Nexus les composants org.iglooproject* d'une version exacte.

Dry-run par défaut ; --delete pour supprimer réellement.
Identifiants : NEXUS_USER / NEXUS_PASSWORD, sinon demandés au clavier.

Utilisation :

python3 nexus-igloo-clean.py 7.9.0
python3 nexus-igloo-clean.py 7.9.0 --delete
"""
import argparse
import getpass
import os
import sys

import requests

BASE_URL = "https://nexus.tools.kobalt.fr/service/rest/v1"

def search(session, repo, version, group_prefix):
    found, token = [], None
    while True:
        params = {"repository": repo, "version": version}
        if token:
            params["continuationToken"] = token
        r = session.get(f"{BASE_URL}/search", params=params, timeout=60)
        r.raise_for_status()
        data = r.json()
        found += [
            c for c in data["items"]
            if c["version"] == version
            and (c["group"] or "").startswith(group_prefix)
        ]
        token = data.get("continuationToken")
        if not token:
            return found

def main():
    p = argparse.ArgumentParser()
    p.add_argument("version")
    p.add_argument("--repo", default="igloo-releases")
    p.add_argument("--group-prefix", default="org.iglooproject")
    p.add_argument("--delete", action="store_true", help="supprime réellement (sinon dry-run)")
    args = p.parse_args()

    session = requests.Session()
    user, password = os.environ.get("NEXUS_USER"), os.environ.get("NEXUS_PASSWORD")
    if args.delete:
        user = user or input("Utilisateur Nexus : ")
        password = password or getpass.getpass("Mot de passe Nexus : ")
    if user and password:
        session.auth = (user, password)

    found = sorted(search(session, args.repo, args.version, args.group_prefix),
                   key=lambda c: (c["group"], c["name"]))
    for c in found:
        print(f"  {c['group']}:{c['name']}:{c['version']}  ({len(c['assets'])} fichiers)")
    print(f"{len(found)} composants, {sum(len(c['assets']) for c in found)} fichiers dans {args.repo}")

    if not args.delete:
        print("Dry-run : rien n'a été supprimé (relancer avec --delete).")
        return
    if not found:
        return
    if input(f"Taper « {args.version} » pour confirmer la suppression : ").strip() != args.version:
        sys.exit("Annulé.")

    errors = 0
    for c in found:
        gav = f"{c['group']}:{c['name']}:{c['version']}"
        r = session.delete(f"{BASE_URL}/components/{c['id']}", timeout=60)
        if r.status_code == 204:
            print(f"supprimé      {gav}")
        elif r.status_code == 404:
            print(f"déjà absent   {gav}")
        else:
            errors += 1
            print(f"ERREUR {r.status_code}  {gav} : {r.text}")
    print(f"Terminé : {len(found) - errors} OK, {errors} erreur(s).")
    sys.exit(1 if errors else 0)

if __name__ == "__main__":
    main()
