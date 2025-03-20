package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AntecedentMedical {

    private  int id_antecedentMedical;
    private  String type_antecedentMedical;
    private  String description_antecedentMedical;
    private  int idPatient;


    public AntecedentMedical() {
    }

    public final AntecedentMedical build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "id_antecedentMedical", "type_antecedentMedical","description_antecedentMedical", "idPatient");
        return this;

    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(id_antecedentMedical),type_antecedentMedical, description_antecedentMedical, String.valueOf(idPatient));
    }

    public AntecedentMedical(String type_antecedentMedical, String description_antecedentMedical, int idPatient) {
        this.type_antecedentMedical = type_antecedentMedical;
        this.description_antecedentMedical = description_antecedentMedical;
        this.idPatient = idPatient;
    }


    public int getId_antecedentMedical() {return id_antecedentMedical;}
    public String getType_antecedentMedical() {
        return type_antecedentMedical;
    }
    public String getDescription_antecedentMedical() {return description_antecedentMedical;}
    public int getIdPatient() {return idPatient;}


    @JsonProperty("antecedentMedical_type_antecedentMedical")
    public void setType_antecedentMedical(String type_antecedentMedical) {this.type_antecedentMedical = type_antecedentMedical;}
    @JsonProperty("antecedentMedical_id_antecedentMedical")
    public void setId_antecedentMedical(int id_antecedentMedical) {
        this.id_antecedentMedical = id_antecedentMedical;
    }
    @JsonProperty("antecedentMedical_description_antecedentMedical")
    public void setDescription_antecedentMedical(String description_antecedentMedical) {this.description_antecedentMedical = description_antecedentMedical;}
    @JsonProperty("antecedentMedical_idPatient")
    public void setIdPatient (int idPatient) {this.idPatient = idPatient;}

    private void setFieldsFromResulset(final ResultSet resultSet, final String ... fieldNames )
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        for(final String fieldName : fieldNames ) {
            final Field field = this.getClass().getDeclaredField(fieldName);
            field.set(this, resultSet.getObject(fieldName));
        }
    }

    private final PreparedStatement buildPreparedStatement(PreparedStatement preparedStatement, final String ... fieldNames )
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        int ix = 0;
        for(final String fieldName : fieldNames ) {
            preparedStatement.setString(++ix, fieldName);
        }
        return preparedStatement;
    }

    @Override
    public String toString() {
        return "AntecedentMedical{" +
                "id_antecedentMedical='" + id_antecedentMedical + '\'' +
                ", type_antecedentMedical='" + type_antecedentMedical + '\'' +
                ", description_antecedentMedical='" + description_antecedentMedical + '\'' +
                ", idPatient='" + idPatient + '\'' +
                '}';
    }
}
