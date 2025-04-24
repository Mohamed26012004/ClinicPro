package edu.ezip.ing1.pds.business.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.AntecedentMedical;
import edu.ezip.ing1.pds.business.dto.AntecedentMedicals;
import edu.ezip.ing1.pds.business.dto.CompteRendu;
import edu.ezip.ing1.pds.business.dto.CompteRendus;
import edu.ezip.ing1.pds.business.dto.Consulte;
import edu.ezip.ing1.pds.business.dto.Diagnostic;
import edu.ezip.ing1.pds.business.dto.Diagnostics;
import edu.ezip.ing1.pds.business.dto.Equipement;
import edu.ezip.ing1.pds.business.dto.Equipements;
import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.business.dto.Horaire;
import edu.ezip.ing1.pds.business.dto.Horaires;
import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.business.dto.Medecins;
import edu.ezip.ing1.pds.business.dto.Paiement;
import edu.ezip.ing1.pds.business.dto.Paiements;
import edu.ezip.ing1.pds.business.dto.Patient;
import edu.ezip.ing1.pds.business.dto.Patients;
import edu.ezip.ing1.pds.business.dto.RendezVous;
import edu.ezip.ing1.pds.business.dto.RendezVouss;
import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.business.dto.Salles;
import edu.ezip.ing1.pds.business.dto.Traitement;
import edu.ezip.ing1.pds.business.dto.Traitements;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.commons.Response;

public class XMartCityService {

    private final static String LoggingLabel = "B u s i n e s s - S e r v e r";
    private final Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private DateTimeFormatter formattage = DateTimeFormatter.ofPattern("HH:mm");

    private enum Queries {

        SELECT_ALL_EXAMENS("SELECT t.id, t.nom, t.cout, t.duree FROM examen t"),
        INSERT_EXAMEN("INSERT into examen (nom, cout, duree) values (?, ?, ?)"),
        UPDATE_EXAMEN("UPDATE examen SET nom = ?, cout = ?, duree = ? WHERE id = ?"),
        DELETE_EXAMEN("DELETE FROM examen WHERE id = ?"),
        SELECT_ONE_EXAMEN("SELECT t.id, t.nom, t.cout, t.duree FROM examen t WHERE nom = ? AND cout = ? AND duree = ?"),
        ID_EXAMEN("SELECT id FROM examen WHERE nom = ? AND cout = ? AND duree = ?"),

        SELECT_ALL_FACTURES("SELECT t.idFacture, t.dateFacture, t.regle FROM facture t"),
        INSERT_FACTURE("INSERT into facture (dateFacture, regle) values (?, ?)"),
        UPDATE_FACTURE("UPDATE facture SET dateFacture = ?, regle = ? WHERE idFacture = ?"),
        DELETE_FACTURE("DELETE FROM facture WHERE idFacture = ?"),
        ID_FACTURE("SELECT idFacture FROM facture WHERE dateFacture = ? AND regle = ?"),

        FACTURES_PAYEES("SELECT t.idFacture, t.dateFacture FROM facture t WHERE t.regle=true"),
        FACTURES_QUOTIDIENNES("SELECT idFacture, regle FROM facture WHERE dateFacture = ?"),

        SELECT_ALL_MEDECINS("SELECT m.numeroADELI, m.nom, m.prenom, m.telephone, m.specialite, m.salaire FROM medecin m"),
        INSERT_MEDECIN("INSERT into medecin (numeroADELI, nom, prenom, telephone, specialite, salaire) values (?, ?, ?, ?, ?, ?)"),
        UPDATE_MEDECIN("UPDATE medecin SET nom = ?, prenom = ?, telephone = ?, specialite = ?, salaire = ? WHERE numeroADELI = ?"),
        DELETE_MEDECIN("DELETE FROM medecin WHERE numeroADELI = ?"),
        SELECT_SPECIALITE_MEDECIN("SELECT m.specialite FROM medecin m"),
        SELECT_MEDECIN_PAR_SPECIALITE("SELECT m.nom, m.prenom FROM medecin m WHERE specialite = ?"),

        SELECT_ALL_HORAIRES("SELECT h.id, h.jour, h.heureDebut, h.heureFin FROM horaire h"),
        INSERT_HORAIRE("INSERT into horaire (jour, heureDebut, heureFin) values (?, ?, ?)"),
        UPDATE_HORAIRE("UPDATE horaire SET jour = ?, heureDebut = ?, heureFin = ? WHERE id = ?"),
        DELETE_HORAIRE("DELETE FROM horaire WHERE id = ?"),
        ID_HORAIRE("SELECT id FROM horaire WHERE jour = ? AND heureDebut = ? AND heureFin = ?"),
        SELECT_ONE_HORAIRE("SELECT h.id, h.jour, h.heureDebut, h.heureFin FROM horaire h WHERE jour = ? AND heureDebut = ? AND heureFin = ?"),

        SELECT_ALL_SALLES("SELECT s.id, s.numeroSalle, s.typeSalle, s.statut FROM salle s"),
        INSERT_SALLE("INSERT into salle (numeroSalle, typeSalle, statut) values (?, ?, ?)"),
        UPDATE_SALLE("UPDATE salle SET numeroSalle = ?, typeSalle = ?, statut = ? WHERE id = ?"),
        DELETE_SALLE("DELETE FROM salle WHERE numeroSalle = ? AND typeSalle = ?"),
        ID_SALLE("SELECT id FROM salle WHERE numeroSalle = ? AND typeSalle = ?"),

        SELECT_ALL_PATIENTS("SELECT p.idPatient, p.nom, p.prenom, p.telephone, p.adresse  FROM patient p"),
        INSERT_PATIENT("INSERT into patient (nom, prenom, telephone, adresse) values (?, ?, ?, ?)"),
        UPDATE_PATIENT("UPDATE patient SET nom = ?, prenom = ?, telephone = ?, adresse = ? WHERE idPatient = ?"),
        DELETE_PATIENT("DELETE FROM patient WHERE nom = ? AND prenom = ? AND telephone = ? AND adresse = ?"),

        SELECT_ALL_RENDEZ_VOUS("SELECT p.numeroADELI, p.idPatient, p.id, p.idSalle, p.dateRendezVous, p.heureDebut, p.heureFin FROM rendezvous p"),
        INSERT_RENDEZ_VOUS("INSERT into rendezvous (numeroADELI, idPatient, id, idSalle, dateRendezVous, heureDebut, heureFin) values (?, ?, ?, ?, ?, ?, ?)"),
        UPDATE_RENDEZ_VOUS("UPDATE rendezvous SET numeroADELI = ?, idPatient = ?, id = ?, idSalle = ?, dateRendezVous = ?, heureDebut = ?, heureFin = ? WHERE idRendezVous = ?"),
        DELETE_RENDEZ_VOUS("DELETE FROM rendezvous WHERE numeroADELI = ? AND idPatient = ? AND id = ?  AND idSalle = ? AND dateRendezVous = ? AND heureDebut = ? AND heureFin = ?"),

        INSERT_CONSULTE("INSERT into consulte (numeroADELI, id) values (?, ?)"),
        SELECT_HORAIRE_MEDECIN("SELECT h.jour, h.heureDebut, h.heureFin FROM horaire h, consulte c, medecin m WHERE m.numeroADELI = ? AND m.numeroADELI = c.numeroADELI AND h.id = c.id "),

        ID_PATIENT("SELECT idPatient FROM patient WHERE nom = ? AND prenom = ? AND telephone = ? AND adresse = ?"),

        SELECT_ALL_PAIEMENTS("SELECT p.idPaiement, p.montant, p.datePaiement, p.moyenDePaiement FROM paiement p"),
        INSERT_PAIEMENT("INSERT into paiement (montant, datePaiement, moyenDePaiement) values (?, ?, ?)"),
        UPDATE_PAIEMENT("UPDATE paiement SET montant = ?, datePaiement = ?, moyenDePaiement = ? WHERE idPaiement = ?"),
        DELETE_PAIEMENT("DELETE FROM paiement WHERE idPaiement = ?"),
        ID_PAIEMENT("SELECT idPaiement FROM paiement WHERE montant = ? AND datePaiement = ? AND moyenDePaiement = ?"),

        SELECT_ALL_ANTECEDENT_MEDICALS("SELECT a.id_antecedentMedical, a.type_antecedentMedical, a.description_antecedentMedical, a.idPatient FROM antecedentMedical a"),
        INSERT_ANTECEDENT_MEDICAL("INSERT into antecedentMedical (idPatient, type_antecedentMedical, description_antecedentMedical) values (?, ?, ?)"),
        UPDATE_ANTECEDENT_MEDICAL("UPDATE antecedentMedical SET type_antecedentMedical = ?, description_antecedentMedical = ?, idPatient = ? WHERE id_antecedentMedical = ?"),
        DELETE_ANTECEDENT_MEDICAL("DELETE FROM antecedentMedical WHERE id_antecedentMedical = ?"),
        ID_ANTECEDENT_MEDICAL("SELECT id FROM antecedentMedical WHERE type_antecedentMedical = ? AND description_antecedentMedical = ? AND idPatient = ?"),

        SELECT_ALL_COMPTE_RENDUS("SELECT c.id_compteRendu, c.typeSymptome, c.descriptionSymptome, c.idPatient, c.numeroADELI FROM compteRendu c"),
        INSERT_COMPTE_RENDU("INSERT into compteRendu (idPatient, numeroADELI, typeSymptome, descriptionSymptome) values (?, ?, ?, ?)"),
        UPDATE_COMPTE_RENDU("UPDATE compteRendu SET typeSymptome = ?, descriptionSymptome = ?, idPatient = ?, numeroADELI = ?  WHERE id_compteRendu = ?"),
        DELETE_COMPTE_RENDU("DELETE FROM compteRendu WHERE id_compteRendu = ?"),
        ID_COMPTE_RENDU("SELECT id FROM compteRendu WHERE typeSymptome = ? AND descriptionSymptome = ? AND idPatient = ? AND numeroADELI = ?"),

        SELECT_ALL_DIAGNOSTICS("SELECT d.id_Diagnostic, d.codeCIM10, d.nomMaladie, d.descriptionDiagnostic, d.idPatient, d.numeroADELI FROM Diagnostic d"),
        INSERT_DIAGNOSTIC("INSERT into Diagnostic (codeCIM10, nomMaladie, descriptionDiagnostic) values (?, ?, ?, ?, ?)"),
        UPDATE_DIAGNOSTIC("UPDATE Diagnostic SET codeCIM10 = ?, nomMaladie = ?, descriptionDiagnostic = ?, idPatient = ?, numeroADELI = ? WHERE id_Diagnostic = ?"),
        DELETE_DIAGNOSTIC("DELETE FROM Diagnostic WHERE id_Diagnostic = ?"),
        ID_DIAGNOSTIC("SELECT id FROM Diagnostic WHERE codeCIM10 = ? AND nomMaladie = ? AND descriptionDiagnostic = ? AND idPatient = ? AND numeroADELI = ?"),

        SELECT_ALL_TRAITEMENTS("SELECT t.id_Traitement, t.typeTraitement, t.descriptionTraitement, t.debutTraitement, t.finTraitement, t.idPatient, t.numeroADELI FROM Traitement t"),
        INSERT_TRAITEMENT("INSERT into Traitement (idPatient, numeroADELI, typeTraitement, descriptionTraitement, debutTraitement, finTraitement) values (?, ?, ?, ?)"),
        UPDATE_TRAITEMENT("UPDATE Traitement SET typeTraitement = ?, descriptionTraitement = ?, debutTraitement = ?, finTraitement = ?, idPatient = ?, numeroADELI = ?  WHERE id_Traitement = ?"),
        DELETE_TRAITEMENT("DELETE FROM Traitement WHERE id_Traitement = ?"),
        ID_TRAITEMENT("SELECT id FROM Traitement WHERE typeTraitement = ? AND descriptionTraitement = ? AND debutTraitement = ? AND finTraitement = ? AND idPatient = ? AND numeroADELI = ?"),


        SELECT_ALL_EQUIPEMENTS("SELECT e.idEquipement, e.coutEquipement, e.dateAchat, e.nomEquipement FROM equipement e "),
        INSERT_EQUIPEMENT("INSERT into equipement (idEquipement, nomEquipement, dateAchat, coutEquipement) values (?, ?, ?, ?)"),
        UPDATE_EQUIPEMENT("UPDATE equipement SET coutEquipement = ?, dateAchat = ?, nomEquipement = ? WHERE idEquipement = ?"),
        DELETE_EQUIPEMENT("DELETE FROM equipement WHERE idEquipement = ? AND coutEquipement = ? AND nomEquipement = ? AND dateAchat = ? "),

        ID_EQUIPEMENT("SELECT idEquipeemnt FROM equipement WHERE coutEquipement = ? AND dateAchat = ?");

        private final String query;

        private Queries(final String query) {
            this.query = query;
        }
    }

    public static XMartCityService inst = null;
    public static final XMartCityService getInstance()  {
        if(inst == null) {
            inst = new XMartCityService();
        }
        return inst;
    }

    private XMartCityService() {

    }

    public final Response dispatch(final Request request, final Connection connection)
            throws InvocationTargetException, IllegalAccessException, SQLException, IOException {
        Response response = null;

        final Queries queryEnum = Enum.valueOf(Queries.class, request.getRequestOrder());
        switch(queryEnum) {
            case SELECT_ALL_EXAMENS:
                response = SelectAllExamens(request, connection);
                break;
            case INSERT_EXAMEN:
                response = InsertExamen(request, connection);
                break;
            case UPDATE_EXAMEN:
                response = UpdateExamen(request, connection);
                break;
            case DELETE_EXAMEN:
                response = DeleteExamen(request, connection);
                break;
            case SELECT_ONE_EXAMEN:
                response = selectOneExamen(request, connection);
                break;



            case SELECT_ALL_FACTURES:
                response = SelectAllFactures(request, connection);
                break;
            case INSERT_FACTURE:
                response = InsertFacture(request, connection);
                break;
            case UPDATE_FACTURE:
                response = UpdateFacture(request, connection);
                break;
            case DELETE_FACTURE:
                response = DeleteFacture(request, connection);
                break;

            case FACTURES_PAYEES :
                response = facturespayees(request, connection);
                break;
            case FACTURES_QUOTIDIENNES :
                response = facturesquotidiennes(request, connection);
                break;



            case INSERT_MEDECIN:
                response = InsertMedecin(request, connection);
                break;
            case SELECT_ALL_MEDECINS:
                response = SelectAllMedecins(request, connection);
                break;
            case SELECT_SPECIALITE_MEDECIN:
                response = SelectSpecialiteMedecin(request, connection);
                break;
            case SELECT_MEDECIN_PAR_SPECIALITE:
                response = SelectMedecinParSpecialite(request, connection);
                break;
            case DELETE_MEDECIN:
                response = DeleteMedecin(request, connection);
                break;
            case UPDATE_MEDECIN:
                response = UpdateMedecin(request, connection);
                break;


            case INSERT_HORAIRE:
                response = InsertHoraire(request, connection);
                break;
            case DELETE_HORAIRE:
                response = DeleteHoraire(request, connection);
                break;
            case SELECT_ALL_HORAIRES:
                response = SelectAllHoraires(request, connection);
                break;
            case SELECT_ONE_HORAIRE:
                response = SelectOneHoraire(request, connection);
                break;
            case UPDATE_HORAIRE:


                response = UpdateHoraire(request, connection);
            case INSERT_SALLE:
                response = InsertSalle(request, connection);
                break;
            case DELETE_SALLE:
                response = DeleteSalle(request, connection);
                break;
            case UPDATE_SALLE:
                response = UpdateSalle(request, connection);
                break;
            case SELECT_ALL_SALLES:
                response = SelectAllSalles(request, connection);
                break;

            case SELECT_ALL_PATIENTS:
                response = SelectAllPatients(request, connection);
                break;
            case INSERT_PATIENT:
                response = InsertPatient(request, connection);
                break;
            case DELETE_PATIENT:
                response = DeletePatient(request, connection);
                break;
            case UPDATE_PATIENT:
                response = UpdatePatient(request, connection);
                break;
            case SELECT_ALL_PAIEMENTS:
                response = SelectAllPaiements(request, connection);
                break;
            case INSERT_PAIEMENT:
                response = InsertPaiement(request, connection);
                break;
            case UPDATE_PAIEMENT:
                response = UpdatePaiement(request, connection);
                break;
            case DELETE_PAIEMENT:
                response = DeletePaiement(request, connection);
                break;


            case SELECT_ALL_ANTECEDENT_MEDICALS:
                response = SelectAllAntecedentMedicals(request, connection);
                break;
            case INSERT_ANTECEDENT_MEDICAL:
                response = InsertAntecedentMedical(request, connection);
                break;
            case UPDATE_ANTECEDENT_MEDICAL:
                response = UpdateAntecedentMedical(request, connection);
                break;
            case DELETE_ANTECEDENT_MEDICAL:
                response = DeleteAntecedentMedical(request, connection);
                break;


            case SELECT_ALL_COMPTE_RENDUS:
                response = SelectAllCompteRendus(request, connection);
                break;
            case INSERT_COMPTE_RENDU:
                response = InsertCompteRendu(request, connection);
                break;
            case UPDATE_COMPTE_RENDU:
                response = UpdateCompteRendu(request, connection);
                break;
            case DELETE_COMPTE_RENDU:
                response = DeleteCompteRendu(request, connection);
                break;

            case SELECT_ALL_DIAGNOSTICS:
                response = SelectAllDiagnostics(request, connection);
                break;
            case INSERT_DIAGNOSTIC:
                response = InsertDiagnostic(request, connection);
                break;
            case UPDATE_DIAGNOSTIC:
                response = UpdateDiagnostic(request, connection);
                break;
            case DELETE_DIAGNOSTIC:
                response = DeleteDiagnostic(request, connection);
                break;


            case SELECT_ALL_TRAITEMENTS:
                response = SelectAllTraitements(request, connection);
                break;
            case INSERT_TRAITEMENT:
                response = InsertTraitement(request, connection);
                break;
            case UPDATE_TRAITEMENT:
                response = UpdateTraitement(request, connection);
                break;
            case DELETE_TRAITEMENT:
                response = DeleteTraitement(request, connection);
                break;
            case INSERT_RENDEZ_VOUS:
                response = InsertRendezVous(request, connection);
                break;
            case DELETE_RENDEZ_VOUS:
                response = DeleteRendezVous(request, connection);
                break;
            case UPDATE_RENDEZ_VOUS:
                response = UpdateRendezVous(request, connection);
                break;
            case SELECT_ALL_RENDEZ_VOUS:
                response = SelectAllRendezVous(request, connection);
                break;
            case INSERT_CONSULTE:
                response = InsertConsulte(request, connection);
                break;
            case SELECT_HORAIRE_MEDECIN:
                response = SelectHoraireMedecin(request, connection);
                break;
            case SELECT_ALL_EQUIPEMENTS:
                response = SelectAllEquipements(request,connection);
                break;
            case INSERT_EQUIPEMENT :
                response = InsertEquipement(request, connection);
                break;
            case UPDATE_EQUIPEMENT:
                response = UpdateEquipement(request, connection);
                break;
            case DELETE_EQUIPEMENT:
                response = DeleteEquipement(request, connection);
                break;

            default:
                break;
        }

        return response;
    }

    //Méthodes des requêtes sur les examens
    private Response InsertExamen(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Examen examen = objectMapper.readValue(request.getRequestBody(), Examen.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_EXAMEN.query);
        stmt.setString(1, examen.getNom());
        stmt.setDouble(2, examen.getCout());
        stmt.setTime(3, Time.valueOf(examen.getDuree()));
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(examen));
    }

    private Response SelectAllExamens(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_EXAMENS.query);
        Examens examens = new Examens();
        while (res.next()) {
            Examen examen = new Examen();
            examen.setId(res.getInt(1));
            examen.setNom(res.getString(2));
            examen.setCout(res.getDouble(3));
            examen.setDuree(res.getTime(4).toLocalTime());
            examens.add(examen);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(examens));
    }

    private Response selectOneExamen(final Request request, final Connection connection) throws SQLException, JsonProcessingException, IOException{
        final ObjectMapper objectMapper = new ObjectMapper();
        final Examen exam = objectMapper.readValue(request.getRequestBody(), Examen.class);
        final PreparedStatement stmt = connection.prepareStatement(Queries.SELECT_ONE_EXAMEN.query);

        stmt.setString(1, exam.getNom());
        stmt.setDouble(2, exam.getCout());
        stmt.setTime(3, Time.valueOf(exam.getDuree()));

        final ResultSet res = stmt.executeQuery();
        Examens examens = new Examens();
        while (res.next()) {
            Examen examen = new Examen();
            examen.setId(res.getInt(1));
            examen.setNom(res.getString(2));
            examen.setCout(res.getDouble(3));
            examen.setDuree(res.getTime(4).toLocalTime());
            examens.add(examen);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(examens));
    }

    private Response UpdateExamen(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Examen examen = objectMapper.readValue(request.getRequestBody(), Examen.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_EXAMEN.query);

        stmt.setString(1, examen.getNom());
        stmt.setDouble(2, examen.getCout());
        stmt.setTime(3, Time.valueOf(examen.getDuree()));
        stmt.setInt(4, examen.getId());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(examen));
    }

    private Response DeleteExamen(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Examen examen = objectMapper.readValue(request.getRequestBody(), Examen.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_EXAMEN.query);

        final PreparedStatement stmt2 = connection.prepareStatement(Queries.ID_EXAMEN.query);
        stmt2.setString(1, examen.getNom());
        stmt2.setDouble(2, examen.getCout());
        stmt2.setTime(3, Time.valueOf(examen.getDuree()));

        final ResultSet res = stmt2.executeQuery();
        res.next();

        stmt.setInt(1, res.getInt(1));
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(examen));
    }

    // Méthodes des requêtes sur les factures

    private Response SelectAllFactures(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_FACTURES.query);
        Factures factures = new Factures();
        while (res.next()) {
            Facture facture = new Facture();
            facture.setIdFacture(res.getInt(1));
            facture.setDateFacture(res.getString(2));
            facture.setRegle(res.getBoolean(3));
            factures.add(facture);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(factures));
    }

    private Response InsertFacture(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Facture facture = objectMapper.readValue(request.getRequestBody(), Facture.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_FACTURE.query);
        stmt.setString(1, facture.getDateFacture());
        stmt.setBoolean(2, facture.getRegle());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(facture));
    }

    private Response UpdateFacture(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Facture facture = objectMapper.readValue(request.getRequestBody(), Facture.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_FACTURE.query);
        stmt.setString(1, facture.getDateFacture());
        stmt.setBoolean(2, facture.getRegle());
        stmt.setInt(3, facture.getIdFacture());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(facture));
    }

    private Response DeleteFacture(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Facture facture = objectMapper.readValue(request.getRequestBody(), Facture.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_FACTURE.query);
        stmt.setInt(1, facture.getIdFacture());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(facture));
    }

    private Response facturespayees (final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.FACTURES_PAYEES.query);
        Factures factures = new Factures();
        while (res.next()) {
            Facture facture = new Facture();
            facture.setIdFacture(res.getInt(1));
            facture.setDateFacture(res.getString(2));
            factures.add(facture);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(factures));
    }

    private Response facturesquotidiennes (final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.FACTURES_QUOTIDIENNES.query);
        Factures factures = new Factures();
        while (res.next()) {
            Facture facture = new Facture();
            facture.setIdFacture(res.getInt(1));
            facture.setRegle(res.getBoolean(2));
            factures.add(facture);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(factures));
    }

    //Méthodes liées à la table Medecin

    private Response InsertMedecin(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Medecin medecin = objectMapper.readValue(request.getRequestBody(), Medecin.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_MEDECIN.query);
        stmt.setInt(1, medecin.getNumeroADELI() );
        stmt.setString(2, medecin.getNom());
        stmt.setString(3, medecin.getPrenom());
        stmt.setString(4, medecin.getTelephone());
        stmt.setString(5, medecin.getSpecialite());
        stmt.setInt(6, medecin.getSalaire());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(medecin));
    }

    private Response SelectAllMedecins(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_MEDECINS.query);
        Medecins medecins = new Medecins();
        while (res.next()) {
            Medecin medecin = new Medecin();
            medecin.setNumeroADELI(res.getInt(1));
            medecin.setNom(res.getString(2));
            medecin.setPrenom(res.getString(3));
            medecin.setTelephone(res.getString(4));
            medecin.setSpecialite(res.getString(5));
            medecin.setSalaire(res.getInt(6));
            medecins.add(medecin);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(medecins));
    }

    private Response SelectSpecialiteMedecin(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_SPECIALITE_MEDECIN.query);
        Medecins medecins = new Medecins();
        while (res.next()) {
            Medecin medecin = new Medecin();
            medecin.setSpecialite(res.getString(1));
            medecins.add(medecin);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(medecins));
    }

    private Response SelectMedecinParSpecialite(final Request request, final Connection connection) throws SQLException, JsonProcessingException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        logger.info("Info medecin " +request.getRequestBody());
        final Medecin medecin = objectMapper.readValue(request.getRequestBody(), Medecin.class);
        final PreparedStatement stmt = connection.prepareStatement(Queries.SELECT_MEDECIN_PAR_SPECIALITE.query);
        stmt.setString(1, medecin.getSpecialite());
        final ResultSet res = stmt.executeQuery();
        Medecins medecins = new Medecins();
        while (res.next()) {
            Medecin m = new Medecin();
            m.setNom(res.getString(1));
            m.setPrenom(res.getString(2));
            medecins.add(m);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(medecins));
    }

    private Response UpdateMedecin(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Medecin medecin = objectMapper.readValue(request.getRequestBody(), Medecin.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_MEDECIN.query);

        stmt.setString(1, medecin.getNom());
        stmt.setString(2, medecin.getPrenom());
        stmt.setString(3, medecin.getTelephone());
        stmt.setString(4, medecin.getSpecialite());
        stmt.setInt(5, medecin.getSalaire());
        stmt.setInt(6, medecin.getNumeroADELI());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(medecin));
    }

    private Response DeleteMedecin(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Medecin medecin = objectMapper.readValue(request.getRequestBody(), Medecin.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_MEDECIN.query);

        stmt.setInt(1, medecin.getNumeroADELI());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(medecin));
    }

    // Méthodes liées à la table Horaire

    private Response InsertHoraire(final Request request, final Connection connection) throws IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Horaire horaire = objectMapper.readValue(request.getRequestBody(), Horaire.class);

        final PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(Queries.INSERT_HORAIRE.query);
            Time debut = Time.valueOf(horaire.getHeureDebut());
            Time fin = Time.valueOf(horaire.getHeureFin());

            stmt.setString(1, horaire.getJour());
            stmt.setTime(2, debut);
            stmt.setTime(3, fin);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(horaire));
    }

    private Response DeleteHoraire(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Horaire horaire = objectMapper.readValue(request.getRequestBody(), Horaire.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_HORAIRE.query);

        final PreparedStatement stmt2 = connection.prepareStatement(Queries.ID_HORAIRE.query);

        Time debut = Time.valueOf(horaire.getHeureDebut());
        Time fin = Time.valueOf(horaire.getHeureFin());

        stmt2.setString(1, horaire.getJour());
        stmt2.setTime(2, debut);
        stmt2.setTime(3, fin);

        final ResultSet res = stmt2.executeQuery();
        res.next();

        stmt.setInt(1, res.getInt(1));
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(horaire));
    }

    private Response UpdateHoraire(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Horaire horaire = objectMapper.readValue(request.getRequestBody(), Horaire.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_HORAIRE.query);

        stmt.setString(1, horaire.getJour());
        stmt.setTime(2, Time.valueOf(horaire.getHeureDebut()));
        stmt.setTime(3, Time.valueOf(horaire.getHeureFin()));
        stmt.setInt(4, horaire.getId());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(horaire));
    }

    private Response SelectAllHoraires(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_HORAIRES.query);
        Horaires horaires = new Horaires();
        while (res.next()) {
            Horaire horaire = new Horaire();
            horaire.setId(res.getInt(1));
            horaire.setJour(res.getString(2));
            horaire.setHeureDebut(res.getTime(3).toLocalTime());
            horaire.setHeureFin(res.getTime(4).toLocalTime());
            horaires.add(horaire);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(horaires));
    }

    private Response SelectOneHoraire(final Request request, final Connection connection) throws SQLException, JsonProcessingException, IOException{
        final ObjectMapper objectMapper = new ObjectMapper();
        final Horaire h = objectMapper.readValue(request.getRequestBody(), Horaire.class);
        final PreparedStatement stmt = connection.prepareStatement(Queries.SELECT_ONE_HORAIRE.query);

        stmt.setString(1, h.getJour());
        stmt.setTime(2, Time.valueOf(h.getHeureDebut()));
        stmt.setTime(3, Time.valueOf(h.getHeureFin()));

        final ResultSet res = stmt.executeQuery();
        Horaires horaires = new Horaires();
        while (res.next()) {
            Horaire horaire = new Horaire();
            horaire.setId(res.getInt(1));
            horaire.setJour(res.getString(2));
            horaire.setHeureDebut(res.getTime(3).toLocalTime());
            horaire.setHeureFin(res.getTime(4).toLocalTime());
            horaires.add(horaire);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(horaires));
    }

    // Méthodes liées à la table Salle

    private Response InsertSalle(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Salle salle = objectMapper.readValue(request.getRequestBody(), Salle.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_SALLE.query);
        stmt.setString(1, salle.getNumeroSalle());
        stmt.setString(2, salle.getTypeSalle());
        stmt.setString(3, salle.getStatut());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(salle));
    }

    private Response DeleteSalle(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Salle salle = objectMapper.readValue(request.getRequestBody(), Salle.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_SALLE.query);

        stmt.setString(1, salle.getNumeroSalle());
        stmt.setString(2, salle.getTypeSalle());

        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(salle));
    }

    private Response UpdateSalle(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Salle salle = objectMapper.readValue(request.getRequestBody(), Salle.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_SALLE.query);

        stmt.setString(1, salle.getNumeroSalle());
        stmt.setString(2, salle.getTypeSalle());
        stmt.setString(3, salle.getStatut());
        stmt.setInt(4, salle.getId());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(salle));
    }

    private Response SelectAllSalles(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_SALLES.query);
        Salles salles = new Salles();
        while (res.next()) {
            Salle salle = new Salle();
            salle.setId(res.getInt(1));
            salle.setNumeroSalle(res.getString(2));
            salle.setTypeSalle(res.getString(3));
            salle.setStatut(res.getString(4));
            salles.add(salle);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(salles));
    }

    // Méthodes liées à la table Patient

    private Response InsertPatient(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Patient patient = objectMapper.readValue(request.getRequestBody(), Patient.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_PATIENT.query);
        stmt.setString(1, patient.getNom());
        stmt.setString(2, patient.getPrenom());
        stmt.setString(3, patient.getTelephone());
        stmt.setString(4, patient.getAdresse());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(patient));
    }

    private Response SelectAllPatients(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_PATIENTS.query);
        Patients patients = new Patients();
        while (res.next()) {
            Patient patient = new Patient();
            patient.setIdPatient(res.getInt(1));
            patient.setNom(res.getString(2));
            patient.setPrenom(res.getString(3));
            patient.setTelephone(res.getString(4));
            patient.setAdresse(res.getString(5));
            patients.add(patient);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(patients));
    }

    private Response UpdatePatient(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Patient patient = objectMapper.readValue(request.getRequestBody(), Patient.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_PATIENT.query);

        stmt.setString(1, patient.getNom());
        stmt.setString(2, patient.getPrenom());
        stmt.setString(3, patient.getTelephone());
        stmt.setString(4, patient.getAdresse());
        stmt.setInt(5, patient.getIdPatient());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(patient));
    }

    private Response DeletePatient(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Patient patient = objectMapper.readValue(request.getRequestBody(), Patient.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_PATIENT.query);

        stmt.setString(1, patient.getNom());
        stmt.setString(2, patient.getPrenom());
        stmt.setString(3, patient.getTelephone());
        stmt.setString(4, patient.getAdresse());

        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(patient));
    }

    // Méthodes liées à la table RendezVous

    private Response InsertRendezVous(final Request request, final Connection connection) throws IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final RendezVous rdv = objectMapper.readValue(request.getRequestBody(), RendezVous.class);

        final PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(Queries.INSERT_RENDEZ_VOUS.query);
            Time debut = Time.valueOf(rdv.getHeureDebut());
            Time fin = Time.valueOf(rdv.getHeureFin());
            Date date = Date.valueOf(rdv.getDateRendezVous());

            stmt.setInt(1, rdv.getNumeroADELI());
            stmt.setInt(2, rdv.getIdPatient());
            stmt.setInt(3, rdv.getId());
            stmt.setInt(4, rdv.getIdSalle());
            stmt.setDate(5,date);
            stmt.setTime(6, debut);
            stmt.setTime(7, fin);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(rdv));
    }

    // Méthodes liées à la table Paiement

    private Response InsertPaiement ( final Request request, final Connection connection) throws
            SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Paiement paiement = objectMapper.readValue(request.getRequestBody(), Paiement.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_PAIEMENT.query);
        stmt.setDouble(1, paiement.getmontant());
        stmt.setString(2, paiement.getdatePaiement());
        stmt.setString(3, paiement.getmoyenDePaiement());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(paiement));
    }

    private Response SelectAllPaiements ( final Request request, final Connection connection) throws
            SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_PAIEMENTS.query);
        Paiements paiements = new Paiements();
        while (res.next()) {
            Paiement paiement = new Paiement();
            paiement.setidPaiement(res.getInt(1));
            paiement.setmontant(res.getDouble(2));
            paiement.setdatePaiement(res.getString(3));
            paiement.setmoyenDePaiement(res.getString(4));
            paiements.add(paiement);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(paiements));
    }

    private Response UpdatePaiement ( final Request request, final Connection connection) throws
            SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Paiement paiement = objectMapper.readValue(request.getRequestBody(), Paiement.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_PAIEMENT.query);
        stmt.setDouble(1, paiement.getmontant());
        stmt.setString(2, paiement.getdatePaiement());
        stmt.setString(3, paiement.getmoyenDePaiement());
        stmt.setInt(4, paiement.getidPaiement());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(paiement));
    }

    private Response DeletePaiement ( final Request request, final Connection connection) throws
            SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Paiement paiement = objectMapper.readValue(request.getRequestBody(), Paiement.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_PAIEMENT.query);
        stmt.setInt(1, paiement.getidPaiement());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(paiement));
    }
    private Response SelectAllRendezVous(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_RENDEZ_VOUS.query);
        RendezVouss rdvs = new RendezVouss();
        while (res.next()) {
            RendezVous rdv = new RendezVous();
            rdv.setNumeroADELI(res.getInt(1));
            rdv.setIdPatient(res.getInt(2));
            rdv.setId(res.getInt(3));
            rdv.setIdSalle(res.getInt(4));
            rdv.setDateRendezVous(res.getDate(5).toLocalDate());
            rdv.setHeureDebut(res.getTime(6).toLocalTime());
            rdv.setHeureFin(res.getTime(7).toLocalTime());
            rdvs.add(rdv);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(rdvs));
    }

    private Response UpdateRendezVous(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final RendezVous rdv = objectMapper.readValue(request.getRequestBody(), RendezVous.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_RENDEZ_VOUS.query);

        stmt.setInt(1, rdv.getNumeroADELI());
        stmt.setInt(2, rdv.getIdPatient());
        stmt.setInt(3, rdv.getId());
        stmt.setInt(4, rdv.getIdSalle());
        stmt.setDate(5, Date.valueOf(rdv.getDateRendezVous()));
        stmt.setTime(6, Time.valueOf(rdv.getHeureDebut()));
        stmt.setTime(7, Time.valueOf(rdv.getHeureFin()));
        stmt.setInt(8, rdv.getIdRendezVous());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(rdv));
    }

    private Response DeleteRendezVous(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final RendezVous rdv = objectMapper.readValue(request.getRequestBody(), RendezVous.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_RENDEZ_VOUS.query);

        stmt.setInt(1, rdv.getNumeroADELI());
        stmt.setInt(2, rdv.getIdPatient());
        stmt.setInt(3, rdv.getId());
        stmt.setInt(4, rdv.getIdSalle());
        stmt.setDate(5, Date.valueOf(rdv.getDateRendezVous()));
        stmt.setTime(6, Time.valueOf(rdv.getHeureDebut()));
        stmt.setTime(7, Time.valueOf(rdv.getHeureFin()));

        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(rdv));
    }

    // Méthodes de CRUD de la table AntecedentMedical

    private Response InsertAntecedentMedical(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final AntecedentMedical antecedentMedical = objectMapper.readValue(request.getRequestBody(), AntecedentMedical.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_ANTECEDENT_MEDICAL.query);
        stmt.setString(1, antecedentMedical.getType_antecedentMedical());
        stmt.setString(2, antecedentMedical.getDescription_antecedentMedical());
        stmt.setInt(3, antecedentMedical.getIdPatient());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(antecedentMedical));
    }

    private Response SelectAllAntecedentMedicals(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_ANTECEDENT_MEDICALS.query);
        AntecedentMedicals antecedentMedicals = new AntecedentMedicals();
        while (res.next()) {
            AntecedentMedical antecedentMedical = new AntecedentMedical();
            antecedentMedical.setId_antecedentMedical(res.getInt(1));
            antecedentMedical.setType_antecedentMedical(res.getString(2));
            antecedentMedical.setDescription_antecedentMedical(res.getString(3));
            antecedentMedical.setIdPatient(res.getInt(4));
            antecedentMedicals.add(antecedentMedical);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(antecedentMedicals));
    }

    private Response UpdateAntecedentMedical(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final AntecedentMedical antecedentMedical = objectMapper.readValue(request.getRequestBody(), AntecedentMedical.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_ANTECEDENT_MEDICAL.query);
        stmt.setString(1, antecedentMedical.getType_antecedentMedical());
        stmt.setString(2, antecedentMedical.getDescription_antecedentMedical());
        stmt.setInt(3, antecedentMedical.getId_antecedentMedical());
        stmt.setInt(4, antecedentMedical.getIdPatient());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(antecedentMedical));
    }

    private Response DeleteAntecedentMedical(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final AntecedentMedical antecedentMedical = objectMapper.readValue(request.getRequestBody(), AntecedentMedical.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_ANTECEDENT_MEDICAL.query);
        stmt.setInt(1, antecedentMedical.getId_antecedentMedical());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(antecedentMedical));
    }


    // Méthodes de CRUD de la table CompteRendu

    private Response InsertCompteRendu(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final CompteRendu compteRendu = objectMapper.readValue(request.getRequestBody(), CompteRendu.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_COMPTE_RENDU.query);
        stmt.setInt(1, compteRendu.getIdPatient());
        stmt.setInt(2, compteRendu.getNumeroADELI());
        stmt.setString(3, compteRendu.getTypeSymptome());
        stmt.setString(4, compteRendu.getDescriptionSymptome());

        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(compteRendu));
    }

    private Response SelectAllCompteRendus(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_COMPTE_RENDUS.query);
        CompteRendus compteRendus = new CompteRendus();
        while (res.next()) {
            CompteRendu compteRendu = new CompteRendu();
            compteRendu.setId_compteRendu(res.getInt(1));
            compteRendu.setIdPatient(res.getInt(2));
            compteRendu.setNumeroADELI(res.getInt(3));
            compteRendu.setDescriptionSymptome(res.getString(4));
            compteRendus.add(compteRendu);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(compteRendus));
    }

    private Response UpdateCompteRendu(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final CompteRendu compteRendu = objectMapper.readValue(request.getRequestBody(), CompteRendu.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_COMPTE_RENDU.query);
        stmt.setInt(1, compteRendu.getIdPatient());
        stmt.setInt(2, compteRendu.getNumeroADELI());
        stmt.setString(3, compteRendu.getTypeSymptome());
        stmt.setString(4, compteRendu.getDescriptionSymptome());
        stmt.setInt(5, compteRendu.getId_compteRendu());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(compteRendu));
    }

    private Response DeleteCompteRendu(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final CompteRendu compteRendu = objectMapper.readValue(request.getRequestBody(), CompteRendu.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_COMPTE_RENDU.query);
        stmt.setInt(1, compteRendu.getId_compteRendu());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(compteRendu));
    }


    // Méthodes de CRUD de la table Diagnostic

    private Response InsertDiagnostic(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Diagnostic diagnostic = objectMapper.readValue(request.getRequestBody(), Diagnostic.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_DIAGNOSTIC.query);
        stmt.setInt(1, diagnostic.getIdPatient());
        stmt.setInt(2, diagnostic.getNumeroADELI());
        stmt.setString(3, diagnostic.getCodeCIM10());
        stmt.setString(4, diagnostic.getNomMaladie());
        stmt.setString(5, diagnostic.getDescription_Diagnostic());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(diagnostic));
    }

    private Response SelectAllDiagnostics(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_DIAGNOSTICS.query);
        Diagnostics diagnostics = new Diagnostics();
        while (res.next()) {
            Diagnostic diagnostic = new Diagnostic();
            diagnostic.setIdPatient(res.getInt(1));
            diagnostic.setNumeroADELI(res.getInt(2));
            diagnostic.setId_Diagnostic(res.getInt(3));
            diagnostic.setCodeCIM10(res.getString(4));
            diagnostic.setNomMaladie(res.getString(5));
            diagnostic.setDescription_Diagnostic(res.getString(6));
            diagnostics.add(diagnostic);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(diagnostics));
    }

    private Response UpdateDiagnostic(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Diagnostic diagnostic = objectMapper.readValue(request.getRequestBody(), Diagnostic.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_DIAGNOSTIC.query);
        stmt.setInt(1, diagnostic.getId_Diagnostic());
        stmt.setInt(2, diagnostic.getIdPatient());
        stmt.setInt(3, diagnostic.getNumeroADELI());
        stmt.setString(4, diagnostic.getCodeCIM10());
        stmt.setString(5, diagnostic.getNomMaladie());
        stmt.setString(6, diagnostic.getDescription_Diagnostic());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(diagnostic));
    }

    private Response DeleteDiagnostic(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Diagnostic diagnostic = objectMapper.readValue(request.getRequestBody(), Diagnostic.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_DIAGNOSTIC.query);
        stmt.setInt(1, diagnostic.getId_Diagnostic());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(diagnostic));
    }


    // Méthodes de CRUD de la table Traitement

    private Response InsertTraitement(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Traitement traitement = objectMapper.readValue(request.getRequestBody(), Traitement.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_TRAITEMENT.query);
        stmt.setInt(1, traitement.getIdPatient());
        stmt.setInt(2, traitement.getNumeroADELI());
        stmt.setString(3, traitement.getType_Traitement());
        stmt.setString(4, traitement.getDescription_Traitement());
        stmt.setString(5, traitement.getDebut_Traitement());
        stmt.setString(6, traitement.getFin_Traitement());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(traitement));
    }

    private Response SelectAllTraitements(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_TRAITEMENTS.query);
        Traitements traitements = new Traitements();
        while (res.next()) {
            Traitement traitement = new Traitement();
            traitement.setIdPatient(res.getInt(1));
            traitement.setNumeroADELI(res.getInt(2));
            traitement.setId_Traitement(res.getInt(3));
            traitement.setType_Traitement(res.getString(4));
            traitement.setDescription_Traitement(res.getString(5));
            traitement.setDebut_Traitement(res.getString(6));
            traitement.setFin_Traitement(res.getString(7));
            traitements.add(traitement);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(traitements));
    }

    private Response UpdateTraitement(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Traitement traitement = objectMapper.readValue(request.getRequestBody(), Traitement.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_TRAITEMENT.query);
        stmt.setInt(1, traitement.getIdPatient());
        stmt.setInt(2, traitement.getNumeroADELI());
        stmt.setString(3, traitement.getType_Traitement());
        stmt.setString(4, traitement.getDescription_Traitement());
        stmt.setString(5, traitement.getDebut_Traitement());
        stmt.setString(6, traitement.getFin_Traitement());
        stmt.setInt(7, traitement.getId_Traitement());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(traitement));
    }

    private Response DeleteTraitement(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Traitement traitement = objectMapper.readValue(request.getRequestBody(), Traitement.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_TRAITEMENT.query);
        stmt.setInt(1, traitement.getId_Traitement());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(traitement));
    }


    // Méthodes liées à la table Consulete (Classe liant un médecin à ses horaires)

    private Response InsertConsulte(final Request request, final Connection connection) throws IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Consulte consulte = objectMapper.readValue(request.getRequestBody(), Consulte.class);

        final PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(Queries.INSERT_CONSULTE.query);
            stmt.setInt(1, consulte.getNumeroADELI());
            stmt.setInt(2, consulte.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(consulte));
    }

    private Response SelectHoraireMedecin(final Request request, final Connection connection) throws SQLException, JsonProcessingException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Medecin medecin = objectMapper.readValue(request.getRequestBody(), Medecin.class);
        final PreparedStatement stmt = connection.prepareStatement(Queries.SELECT_HORAIRE_MEDECIN.query);
        stmt.setInt(1, medecin.getNumeroADELI());
        final ResultSet res = stmt.executeQuery();
        Horaires horaires = new Horaires();
        while (res.next()) {
            Horaire h = new Horaire();
            h.setJour(res.getString(1));
            h.setHeureDebut(res.getTime(2).toLocalTime());
            h.setHeureFin(res.getTime(3).toLocalTime());
            horaires.add(h);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(horaires));
    }
    // Méthodes des requêtes sur les equipements

    private Response SelectAllEquipements(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_EQUIPEMENTS.query);
        Equipements equipements = new Equipements();
        while (res.next()) {
            Equipement equipement = new Equipement();
            equipement.setIdEquipement(res.getInt(1));
            equipement.setCoutEquipement(res.getInt(2));
            equipement.setDateEquipement(res.getDate(3).toLocalDate());
            equipement.setNomEquipement(res.getString(4));
            equipements.add(equipement);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(equipements));
    }

    private Response InsertEquipement(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Equipement equipement = objectMapper.readValue(request.getRequestBody(), Equipement.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_EQUIPEMENT.query);
        stmt.setInt(1,equipement.getIdEquipement());
        stmt.setString(2, equipement.getNomEquipement());
        stmt.setDate(3, Date.valueOf(equipement.getDateEquipement()));
        stmt.setInt(4, equipement.getCoutEquipement());

        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(equipement));
    }

    private Response UpdateEquipement(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Equipement equipement = objectMapper.readValue(request.getRequestBody(), Equipement.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_EQUIPEMENT.query);
        stmt.setDate(2, Date.valueOf(equipement.getDateEquipement()));
        stmt.setString(3, equipement.getNomEquipement());
        stmt.setInt(1, equipement.getCoutEquipement());
        stmt.setInt(4, equipement.getIdEquipement());

        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(equipement));
    }

    private Response DeleteEquipement(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Equipement equipement = objectMapper.readValue(request.getRequestBody(), Equipement.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_EQUIPEMENT.query);
        stmt.setInt(1, equipement.getIdEquipement());
        stmt.setInt(2, equipement.getCoutEquipement());
        stmt.setString(3, equipement.getNomEquipement());
        stmt.setDate(4, Date.valueOf(equipement.getDateEquipement()));;
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(equipement));
    }



}



