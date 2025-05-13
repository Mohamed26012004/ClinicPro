
CREATE DATABASE clinicprodatabase;
CREATE TABLE paiement (
    idPaiement INT PRIMARY KEY AUTO_INCREMENT,
    montane DOUBLE NOT NULL,
    datePaiement VARCHAR(50) NOT NULL,
    moneyDePaiement VARCHAR(50) NOT NULL
);

CREATE TABLE patient(
   idPatient INT AUTO_INCREMENT,
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50) NOT NULL,
   adresse VARCHAR(50),
   telephone VARCHAR(30) NOT NULL,
   PRIMARY KEY(idPatient)
);

CREATE TABLE medecin(
   numeroADELI INT,
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50) NOT NULL,
   telephone VARCHAR(50) NOT NULL,
   specialite VARCHAR(50) NOT NULL,
   salaire INT NOT NULL,
   PRIMARY KEY(numeroADELI)
);
CREATE TABLE horaire(
   id INT AUTO_INCREMENT,
   jour VARCHAR(50) NOT NULL,
   heureDebut TIME NOT NULL,
   heureFin TIME NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE examen(
   id INT AUTO_INCREMENT,
   nom VARCHAR(50) NOT NULL,
   cout DOUBLE NOT NULL,
   duree TIME NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE antecedentMedical(
   id_antecedentMedical INT AUTO_INCREMENT,
   type_antecedentMedical VARCHAR(50),
   description_antecedentMedical VARCHAR(50),
   idPatient INT NOT NULL,
   PRIMARY KEY(id_antecedentMedical),
   FOREIGN KEY(idPatient) REFERENCES patient(idPatient)
);

CREATE TABLE salle(
   id INT AUTO_INCREMENT,
   numeroSalle VARCHAR(10) NOT NULL,
   typeSalle VARCHAR(50),
   statut VARCHAR(50) NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE paiement (
    idPaiement INT PRIMARY KEY AUTO_INCREMENT,
    montane DOUBLE NOT NULL,
    datePaiement VARCHAR(50) NOT NULL,
    moyenDePaiement VARCHAR(50) NOT NULL
);

CREATE TABLE consulte(
   numeroADELI INT,
   id INT,
   PRIMARY KEY(numeroADELI, id),
   FOREIGN KEY(numeroADELI) REFERENCES medecin(numeroADELI),
   FOREIGN KEY(id) REFERENCES horaire(id)
);

CREATE TABLE equipement(
   idEquipement INT,
   nomEquipement VARCHAR(50) NOT NULL,
   dateAchat DATE NOT NULL,
   coutEquipement DECIMAL(15,2) NOT NULL,
   PRIMARY KEY(idEquipement)
);

CREATE TABLE planification(
   idPlanification INT AUTO_INCREMENT,
   numeroADELI INT NOT NULL,
   idPatient INT NOT NULL,
   idExamen INT NOT NULL,
   idSalle INT,
   datePlanification DATE NOT NULL,
   heureDebut TIME NOT NULL,
   heureFin TIME NOT NULL,
   PRIMARY KEY(idPlanification),
   FOREIGN KEY(idSalle) REFERENCES salle(id),
   FOREIGN KEY(idExamen) REFERENCES examen(id),
   FOREIGN KEY(idPatient) REFERENCES patient(idPatient),
   FOREIGN KEY(numeroADELI) REFERENCES medecin(numeroADELI)
);