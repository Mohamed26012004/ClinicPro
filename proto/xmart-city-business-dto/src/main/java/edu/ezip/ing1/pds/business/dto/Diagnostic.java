
package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "Diagnostic")
public class Diagnostic {
    private  int id_Diagnostic;
    private  String codeCIM10;
    private  String nomMaladie;
    private  String description_Diagnostic;
    private int idPlanification;



    public Diagnostic() {
    }

    public final Diagnostic build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "id_Diagnostic", "codeCIM10", "nomMaladie", "description_Diagnostic", "idPlanification");
        return this;

    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(id_Diagnostic), String.valueOf(idPlanification), codeCIM10, nomMaladie, description_Diagnostic);
    }

    public Diagnostic(String codeCIM10, String nomMaladie, String description_Diagnostic, int idPlanification) {
        this.codeCIM10 = codeCIM10;
        this.nomMaladie = nomMaladie;
        this.description_Diagnostic = description_Diagnostic;
        this.idPlanification = idPlanification;

    }


    public int getId_Diagnostic() {return id_Diagnostic;}
    public String getCodeCIM10() {return codeCIM10;}
    public String getNomMaladie() {return nomMaladie;}
    public String getDescription_Diagnostic() {return description_Diagnostic;}
    public int getIdPlanification() {return idPlanification;}


    @JsonProperty("Diagnostic_codeCIM10")
    public void setCodeCIM10(String codeCIM10) {this.codeCIM10 = codeCIM10;}
    @JsonProperty("Diagnostic_nomMaladie")
    public void setNomMaladie(String nomMaladie) {this.nomMaladie = nomMaladie;}
    @JsonProperty("Diagnostic_id_Diagnostic")
    public void setId_Diagnostic(int id_Diagnostic) {this.id_Diagnostic = id_Diagnostic;}
    @JsonProperty("Diagnostic_description_Diagnostic")
    public void setDescription_Diagnostic(String description_Diagnostic) {this.description_Diagnostic = description_Diagnostic;}
    @JsonProperty("Diagnostic_idPlanification")
    public void setIdPlanification(int idPlanification) {this.idPlanification = idPlanification;}


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
        return "Diagnostic{" +
                "id_Diagnostic='" + id_Diagnostic + '\'' +
                ", codeCIM10='" + codeCIM10 + '\'' +
                ", nomMaladie='" + nomMaladie + '\'' +
                ", description_Diagnostic='" + description_Diagnostic + '\'' +
                "idPlanification='" + idPlanification + '\'' +
                '}';
    }

}

