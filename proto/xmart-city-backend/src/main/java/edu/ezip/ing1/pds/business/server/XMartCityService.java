package edu.ezip.ing1.pds.business.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.format.DateTimeFormatter;

import edu.ezip.ing1.pds.business.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.commons.Response;

public class XMartCityService {

    private final static String LoggingLabel = "B u s i n e s s - S e r v e r";
    private final Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private DateTimeFormatter formattage = DateTimeFormatter.ofPattern("HH:mm");

    private enum Queries {
        
        SELECT_ALL_EXAMENS("SELECT t.nom, t.cout, t.numeroSalle, t.id FROM examen t"),
        INSERT_EXAMEN("INSERT into examen (nom, cout, numeroSalle) values (?, ?, ?)"),
        UPDATE_EXAMEN("UPDATE examen SET nom = ?, cout = ?, numeroSalle = ? WHERE id = ?"),
        DELETE_EXAMEN("DELETE FROM examen WHERE id = ?"),
        SELECT_ONE_EXAMEN("SELECT t.nom, t.cout, t.numeroSalle, t.id FROM examen t WHERE nom = ? AND cout = ? AND numeroSalle = ?"),
        ID_EXAMEN("SELECT id FROM examen WHERE nom = ? AND cout = ? AND numeroSalle = ?"),

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

        SELECT_ALL_HORAIRES("SELECT h.id, h.jour, h.heureDebut, h.heureFin FROM horaire h"),
        INSERT_HORAIRE("INSERT into horaire (jour, heureDebut, heureFin) values (?, ?, ?)"),
        UPDATE_HORAIRE("UPDATE horaire SET jour = ?, heureDebut = ?, heureFin = ? WHERE id = ?"),
        DELETE_HORAIRE("DELETE FROM horaire WHERE id = ?"),
        ID_HORAIRE("SELECT id FROM horaire WHERE jour = ? AND heureDebut = ? AND heureFin = ?"),

        SELECT_ALL_SALLES("SELECT s.id, s.numeroSalle, s.typeSalle, s.statut FROM salle s"),
        INSERT_SALLE("INSERT into salle (numeroSalle, typeSalle, statut) values (?, ?, ?)"),
        UPDATE_SALLE("UPDATE salle SET numeroSalle = ?, typeSalle = ?, statut = ? WHERE id = ?"),
        DELETE_SALLE("DELETE FROM salle WHERE id = ?"),
        ID_SALLE("SELECT id FROM salle WHERE numeroSalle = ? AND typeSalle = ?"),

        SELECT_ALL_PATIENTS("SELECT p.idPatient, p.nom, p.prenom, p.telephone, p.adresse  FROM patient p"),
        INSERT_PATIENT("INSERT into patient (nom, prenom, telephone, adresse) values (?, ?, ?, ?)"),
        UPDATE_PATIENT("UPDATE patient SET nom = ?, prenom = ?, telephone = ?, adresse = ? WHERE idPatient = ?"),
        DELETE_PATIENT("DELETE FROM patient WHERE nom = ? AND prenom = ? AND telephone = ? AND adresse = ?"),
        ID_PATIENT("SELECT idPatient FROM patient WHERE nom = ? AND prenom = ? AND telephone = ? AND adresse = ?");

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
        stmt.setString(3, examen.getNumeroSalle());
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
            examen.setNom(res.getString(1));
            examen.setCout(res.getDouble(2));
            examen.setNumeroSalle(res.getString(3));
            examen.setId(res.getInt(4));
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
        stmt.setString(3, exam.getNumeroSalle());

        final ResultSet res = stmt.executeQuery();
        Examens examens = new Examens();
        while (res.next()) {
            Examen examen = new Examen();
            examen.setNom(res.getString(1));
            examen.setCout(res.getDouble(2));
            examen.setNumeroSalle(res.getString(3));
            examen.setId(res.getInt(4));
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
        stmt.setString(3, examen.getNumeroSalle());
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
        stmt2.setString(3, examen.getNumeroSalle());

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
            facture.setDateFacture(java.sql.Date.valueOf(res.getDate(2).toLocalDate()));
            facture.setRegle(res.getBoolean(3));
            factures.add(facture);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(factures));
    }

    private Response InsertFacture(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Facture facture = objectMapper.readValue(request.getRequestBody(), Facture.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_FACTURE.query);
        stmt.setDate(1, new java.sql.Date(facture.getDateFacture().getTime()));
        stmt.setBoolean(2, facture.getRegle());
        stmt.executeUpdate();

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(facture));
    }

    private Response UpdateFacture(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Facture facture = objectMapper.readValue(request.getRequestBody(), Facture.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_FACTURE.query);
        stmt.setDate(1, new java.sql.Date(facture.getDateFacture().getTime()));
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
            facture.setDateFacture(res.getDate(2));
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

            int rowsInserted = stmt.executeUpdate();
            System.out.println("Nombre de lignes insérées : " + rowsInserted);
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

        final PreparedStatement stmt2 = connection.prepareStatement(Queries.ID_SALLE.query);
        stmt2.setString(1, salle.getNumeroSalle());
        stmt2.setString(2, salle.getTypeSalle());

        final ResultSet res = stmt2.executeQuery();
        res.next();

        stmt.setInt(1, res.getInt(1));
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


}