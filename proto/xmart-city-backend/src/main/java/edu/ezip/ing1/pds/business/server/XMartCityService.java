package edu.ezip.ing1.pds.business.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.business.dto.Factures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;

import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.commons.Response;

public class XMartCityService {

    private final static String LoggingLabel = "B u s i n e s s - S e r v e r";
    private final Logger logger = LoggerFactory.getLogger(LoggingLabel);

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
        FACTURES_QUOTIDIENNES("SELECT idFacture, regle FROM facture WHERE dateFacture = ?");

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

            default:
                break;
        }

        return response;
    }

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



}