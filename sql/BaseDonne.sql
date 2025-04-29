-- Active: 1744738886314@@127.0.0.1@3306@cahiertexte
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

-- Insertion de quelques enseignants
INSERT INTO users (nom, email, mot_de_passe, role)
VALUES 
('Maguette Diagne', 'maguette@univ-thies.sn', '1234', 'enseignant'),
('Binetou Sarr', 'binetou@univ-thies.sn', '1234', 'enseignant'),
('Amadou Diop', 'amadou.diop@univ-thies.sn', '1234', 'enseignant');

-- Insertion de quelques responsables de classe
INSERT INTO users (nom, email, mot_de_passe, role)
VALUES 
('Abdoulaye Diallo', 'diallo@univ-thies.sn', '1234', 'responsable'),
('Fatou Sow', 'fatou.sow@univ-thies.sn', '1234', 'responsable');

-- Insertion des classes avec responsables directement associés
INSERT INTO classes (nom, responsable_id)
VALUES 
('L1 Informatique', 4), -- Abdoulaye est responsable de L1 Info
('L2 Informatique', 5), -- Fatou est responsable de L2 Info
('L3 GLSI', NULL);      -- Pas encore de responsable assigné

-- Insertion de quelques cours
INSERT INTO cours (nom, enseignant_id, classe_id)
VALUES 
('Programmation Orientée Objet', 2, 2), -- Binetou enseigne POO en L2
('Bases de Données', 3, 2), -- Amadou enseigne BD en L2
('Algorithmes et Structures de Données', 2, 1); -- Binetou enseigne Algo en L1

-- Insertion de quelques séances
INSERT INTO seances (cours_id, date_seance, contenu, validee)
VALUES 
(1, '2025-04-15', 'Introduction à la POO et concepts fondamentaux', FALSE),
(1, '2025-04-22', 'Héritage et polymorphisme en Java', FALSE),
(2, '2025-04-16', 'Modèle relationnel et normalisation', FALSE);