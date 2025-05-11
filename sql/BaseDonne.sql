-- Active: 1737733567272@@127.0.0.1@3306
-- Script complet de création de la base de données pour l'application de gestion du cahier de texte
-- Création de la base de données
DROP DATABASE IF EXISTS CahierTexte;
CREATE DATABASE CahierTexte;
USE CahierTexte;

-- Création de la table USERS (utilisateurs)
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('chef', 'enseignant', 'responsable'))
);

-- Création de la table CLASSES (structure simplifiée)
CREATE TABLE classes (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    responsable_id INTEGER,
    FOREIGN KEY (responsable_id) REFERENCES users(id)
);

-- Création de la table COURS
CREATE TABLE cours (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    enseignant_id INTEGER,
    classe_id INTEGER,
    FOREIGN KEY (enseignant_id) REFERENCES users(id),
    FOREIGN KEY (classe_id) REFERENCES classes(id)
);

-- Création de la table SEANCES
CREATE TABLE seances (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    cours_id INTEGER,
    date_seance DATE NOT NULL,
    contenu TEXT,
    validee BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (cours_id) REFERENCES cours(id)
);

-- Création de la table VALIDATIONS
CREATE TABLE validations (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    seance_id INTEGER,
    responsable_id INTEGER,
    date_validation DATE NOT NULL,
    commentaire TEXT,
    FOREIGN KEY (seance_id) REFERENCES seances(id),
    FOREIGN KEY (responsable_id) REFERENCES users(id)
);

-- Insertion des données initiales

-- Utilisateur chef de département
INSERT INTO users (nom, email, mot_de_passe, role)
VALUES ('Mouhamadou Thiam', 'mouhamadou.thiam@univ-thies.sn', 'qwerty123', 'chef');
INSERT INTO users (nom, email, mot_de_passe, role)
VALUES ('Idrissa gaye', 'idrissa.gaye@univ-thies.sn', 'qwerty123', 'chef');
SELECT*from users WHERE role='responsable';