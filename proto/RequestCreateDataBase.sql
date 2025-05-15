
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

CREATE TABLE salle(
   id INT AUTO_INCREMENT,
   numeroSalle VARCHAR(10) NOT NULL,
   typeSalle VARCHAR(50),
   statut VARCHAR(50) NOT NULL,
   PRIMARY KEY(id)
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

CREATE TABLE disponibilite(
   idDisponiblilite INT NOT NULL AUTO_INCREMENT,
   dateDisponibilite DATE NOT NULL,
   heureDebut TIME NOT NULL,
   heureFin TIME NOT NULL,
   statut VARCHAR(50) NOT NULL,
   numeroADELI INT NOT NULL,
   PRIMARY KEY(idDisponiblilite),
   FOREIGN KEY(numeroADELI) REFERENCES Medecin(numeroADELI)
);


CREATE TABLE antecedentMedical(
   id_antecedentMedical INT AUTO_INCREMENT,
   type_antecedentMedical VARCHAR(50),
   description_antecedentMedical VARCHAR(50),
   idPatient INT NOT NULL,
   PRIMARY KEY(id_antecedentMedical),
   FOREIGN KEY(idPatient) REFERENCES patient(idPatient)
);

CREATE TABLE compteRendu(
   id_compteRendu INT AUTO_INCREMENT,
   typeSymptome VARCHAR(50) NOT NULL,
   descriptionSymptome VARCHAR(50) NOT NULL,
   idPlanification INT NOT NULL,
   PRIMARY KEY(id_compteRendu),
   FOREIGN KEY(idPlanification) REFERENCES planification(idPlanification)

);

CREATE TABLE Diagnostic(
   id_Diagnostic INT AUTO_INCREMENT,
   codeCIM10 VARCHAR(50),
   nomMaladie VARCHAR(50),
   descriptionDiagnostic VARCHAR(50) NOT NULL,
   idPlanification INT NOT NULL,
   PRIMARY KEY(id_Diagnostic),
   FOREIGN KEY(idPlanification) REFERENCES planification(idPlanification)
);

CREATE TABLE Traitement(
   Id_Traitement INT AUTO_INCREMENT,
   typeTraitement VARCHAR(50),
   descriptionTraitement VARCHAR(50),
   debutTraitement VARCHAR(50),
   finTraitement VARCHAR(50),
   idPlanification INT NOT NULL,
   PRIMARY KEY(Id_Traitement),
   FOREIGN KEY(idPlanification) REFERENCES planification(idPlanification)
);