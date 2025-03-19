
package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "facture")
public class Facture {
    private  int idFacture;
    private  String dateFacture;
    private  Boolean regle;


    public Facture() {
    }
    public final Facture build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idFacture", "datefacture","regle");
        return this;

    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idFacture), dateFacture, regle.toString());
    }

    public Facture(boolean regle, String dateFacture) {
        this.regle = regle;
        this.dateFacture = dateFacture;
    }

    public boolean  getRegle() {
        return regle;
    }

    public int  getIdFacture() {
        return idFacture;
    }


    public String getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    @JsonProperty("facture_idFacture")
    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    @JsonProperty("facture_regle")
    public void setRegle(boolean regle) {
        this.regle = regle;
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



}

