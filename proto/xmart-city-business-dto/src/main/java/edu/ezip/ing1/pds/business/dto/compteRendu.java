
package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "compteRendu")
public class compteRendu {
    private  int id_compteRendu;
    private  String symptome;


    public compteRendu() {
    }

    public final compteRendu build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "id_antecedentMedical","symptome");
        return this;

    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(id_compteRendu),symptome);
    }

    public compteRendu(String symptome) {
        this.symptome = symptome;
    }

    public int getId_compteRendu() {return id_compteRendu;}
    public String getSymptome() {
        return symptome;
    }

    @JsonProperty("compteRendu_symptome")
    public void setSymptome(String Symptome) {this.symptome = symptome;}
    @JsonProperty("compteRendu_id_compteRendu")
    public void setId_compteRendu(int id_antecedentMedical) {
        this.id_compteRendu = id_compteRendu;
    }

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
        return "compteRendu{" +
                "id_compteRendu='" + id_compteRendu + '\'' +
                ", symptome='" + symptome + '\'' +
                '}';
    }

}

