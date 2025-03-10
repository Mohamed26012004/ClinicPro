--
-- Name: examen; Type: TABLE; Schema: ezip_ing1
--
CREATE TABLE examen (
    id INT AUTO_INCREMENT,
    nom VARCHAR (50),
    cout DOUBLE,
    numeroSalle VARCHAR (50),
    PRIMARY KEY (id)
);

--
-- Name: facture; Type: TABLE; Schema: ezip_ing1
--

CREATE TABLE facture (
    idFacture INT(20) NOT NULL AUTO_INCREMENT,
    regle BOOLEAN NOT NULL,
    dateFacture DATE NOT NULL,
    PRIMARY KEY (idFacture)
);

INSERT INTO facture (regle, dateFacture) VALUES (FALSE, '2025-02-20');