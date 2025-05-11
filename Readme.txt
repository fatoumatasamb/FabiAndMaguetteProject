# 📘 Gestion numérique du cahier de texte avec génération d’une fiche pédagogique

## 📌 Description

Ce projet a pour objectif de développer une application Java permettant de dématérialiser le cahier de texte utilisé dans les établissements scolaires. Il s'agit d'un outil destiné à améliorer le suivi pédagogique, la planification des cours et la communication entre les enseignants, les responsables de classe et le chef de département.

## 👤 Utilisateurs et rôles

L'application est accessible à trois types d'utilisateurs avec des fonctionnalités distinctes :

- **Chef de département**
  - Ajouter des enseignants et des responsables de classe
  - Assigner un ou plusieurs cours à un enseignant
  - Générer une fiche de suivi pédagogique (PDF ou Excel)

- **Enseignant**
  - Voir la liste de ses cours
  - Ajouter une séance et son contenu

- **Responsable de classe**
  - Consulter le cahier de texte
  - Valider le contenu ajouté par l’enseignant

## 🧩 Fonctionnalités principales

- Authentification par rôle
- Interface pour la saisie et la consultation du cahier de texte
- Gestion des utilisateurs et attribution des rôles
- Visualisation des séances ajoutées
- Exportation des fiches de suivi pédagogique

## 🛠️ Technologies utilisées

- **Langage** : Java
- **Interface graphique** : Swing
- **Base de données** : MySQL 
- **IDE** : Visual Studio Code
- **Gestion de projet** : Git & GitHub

## 🚀 Installation et exécution

1. **Cloner le dépôt Git :**
   ```bash
   git clone <lien_du_dépôt>
   cd projet-cahier-de-texte
   ```

2. **Configurer la base de données :**
   - Créer une base de données MySQL .
   - Exécuter les scripts SQL de création des tables (fournis dans le dossier `database/` si disponible).
   - Mettre à jour les paramètres de connexion dans le code (`DBConnection.java` ou équivalent).

3. **Compiler et exécuter le projet :**
   - Ouvrir le projet avec VS Code.
   - S'assurer que les extensions Java sont installées.
   - Lancer la classe principale (`main.java` ).

## 📂 Organisation du projet

- `src/` : Contient les fichiers source Java (interfaces, modèles, contrôleurs)
- `BaseDonne.sql/` : Scripts SQL de création de la base
- `readme.txt` : Ce fichier

## ✍️ Auteurs

- Fatoumata Bintou Samb
- Maguette Diop

## 📅 Date de présentation

- Dimanche 11 Mai à 08h00

## 📄 Licence

Ce projet est académique. Toute réutilisation doit mentionner l’auteur.