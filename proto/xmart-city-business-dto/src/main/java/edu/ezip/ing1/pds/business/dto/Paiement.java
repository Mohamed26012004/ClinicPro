package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "paiement")
public class Paiement {

    private  Date datePaiement;
    private  int montant;

    public Paiement(){
    }

    public final Paiement build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "datePaiement", "montant");
    return this;
}

public final PreparedStatement build(PreparedStatement preparedStatement)
    throws SQLException, NoSuchFieldException, IllegalAccessException {
return buildPreparedStatement(preparedStatement, "datePaiement", String.valueOf(montant));
}

    public Paiement(Date datePaiement, int montant) {
        this.datePaiement = datePaiement;
        this.montant = montant;
    }

    public Date getdatePaiement() {
        return datePaiement;
    }
    public int getmontant() {
        return montant;
    }


    @JsonProperty("paiement_datePaiement")
    public void setdatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

   
    @JsonProperty("paiement_montant")
    public void setmontant(int montant) {
        this.montant = montant;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Paiement{");
        sb.append("datePaiement=").append(datePaiement);
        sb.append(", montant=").append(montant);
        sb.append('}');
        return sb.toString();
    }

    

}

