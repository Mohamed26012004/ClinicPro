package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonRootName(value = "paiement")
public class Paiement {
    private int idPaiement;
    private  double montant;
    private  String datePaiement;
    private  String moyenDePaiement;

    public Paiement() {
    }
    public final Paiement build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idPaiement", "montant","datePaiement", "moyenDePaiement");
        return this;
    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idPaiement), String.valueOf(montant), datePaiement, moyenDePaiement);
    }

    public Paiement(double montant, String datePaiement, String moyenDePaiement) {
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.moyenDePaiement = moyenDePaiement;
    }

    public int getidPaiement() {
        return idPaiement;
    }
    public double getmontant() {
        return montant;
    }
    public String getdatePaiement() {
        return datePaiement;
    }
    public String getmoyenDePaiement() {
        return moyenDePaiement;
    }
    
    @JsonProperty("idPaiement")
    public void setidPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    @JsonProperty("montant")
    public void setmontant(double montant) {
        this.montant = montant;
    }

    @JsonProperty("datePaiement")
    public void setdatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    @JsonProperty("moyenDePaiement")
    public void setmoyenDePaiement(String moyenDePaiement) {
        this.moyenDePaiement = moyenDePaiement;
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
        return "Paiement{" +
                "idPaiement=" + idPaiement +
                ", montant='" + montant + '\'' +
                ", datePaiement='" + datePaiement + '\'' +
                ", moyenDePaiement='" + moyenDePaiement + '\'' +
                '}';
    }
}