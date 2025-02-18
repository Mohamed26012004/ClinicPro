package edu.ezip.ing1.pds.business.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.business.dto.Student;
import edu.ezip.ing1.pds.business.dto.Students;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.commons.Response;

public class XMartCityService {

    private final static String LoggingLabel = "B u s i n e s s - S e r v e r";
    private final Logger logger = LoggerFactory.getLogger(LoggingLabel);

    private enum Queries {
        SELECT_ALL_STUDENTS("SELECT t.name, t.firstname, t.groupname, t.id FROM students t"),
        INSERT_STUDENT("INSERT into students (name, firstname, groupname) values (?, ?, ?)"),
        
        SELECT_ALL_EXAMENS("SELECT t.nom, t.cout, t.numeroSalle, t.id FROM examen t"),
        INSERT_EXAMEN("INSERT into examen (nom, cout, numeroSalle) values (?, ?, ?)"),
        UPDATE_EXAMEN("UPDATE examen SET nom = ?, cout = ?, numeroSalle = ? WHERE id = ?"),
        DELETE_EXAMEN("DELETE FROM examen WHERE id = ?"),
        ID_EXAMEN("SELECT id FROM examen WHERE nom = ? AND cout = ? AND numeroSalle = ?");

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

//        final Statement stmt2 = connection.createStatement();
//        final ResultSet res = stmt2.executeQuery("SELECT LAST_INSERT_ID()");
//        res.next();
//
//        examen.setId(res.getInt(1));

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

    private Response UpdateExamen(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Examen examen = objectMapper.readValue(request.getRequestBody(), Examen.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_EXAMEN.query);

        final PreparedStatement stmt2 = connection.prepareStatement(Queries.ID_EXAMEN.query);
        stmt2.setString(1, examen.getNom());
        stmt2.setDouble(2, examen.getCout());
        stmt2.setString(3, examen.getNumeroSalle());

        final ResultSet res = stmt2.executeQuery();
        res.next();

        stmt.setString(1, examen.getNom());
        stmt.setDouble(2, examen.getCout());
        stmt.setString(3, examen.getNumeroSalle());
        stmt.setInt(4, res.getInt(1));
        stmt.executeUpdate();

            // final Statement stmt2 = connection.createStatement();
            // final ResultSet res = stmt2.executeQuery("SELECT LAST_INSERT_ID()");
            // res.next();

            // examen.setId(res.getInt(1));

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

        //examen.setId(res.getInt(1));

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(examen));
    }

     private Response InsertStudent(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Student student = objectMapper.readValue(request.getRequestBody(), Student.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_STUDENT.query);
        stmt.setString(1, student.getName());
        stmt.setString(2, student.getFirstname());
        stmt.setString(3, student.getGroup());
        stmt.executeUpdate();

        final Statement stmt2 = connection.createStatement();
        final ResultSet res = stmt2.executeQuery("SELECT LAST_INSERT_ID()");
        res.next();

        student.setId(res.getInt(1));

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(student));
    }



    private Response SelectAllStudents(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
       final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_STUDENTS.query);
        Students students = new Students();
        while (res.next()) {
            Student student = new Student();
            student.setName(res.getString(1));
            student.setFirstname(res.getString(2));
            student.setGroup(res.getString(3));
            student.setId(res.getInt(4));
            students.add(student);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(students));
    }
    
}