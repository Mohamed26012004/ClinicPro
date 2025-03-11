package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonRootName(value = "patient")
public class Patient {

    private int idPatient;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;

    public Patient() {
    }
    public final Patient build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idPatient", "nom","prenom", "telephone", "adresse");
        return this;
    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idPatient), nom, prenom, telephone, adresse);
    }
    public Patient(String nom, String prenom, String telephone, String adresse){
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public int getIdPatient() {
        return idPatient;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getTelephone() {
        return telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    @JsonProperty("patient_idPatient")
    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }
    @JsonProperty("patient_nom")
    public void setNom(String nom) {
        this.nom = nom;
    }
    @JsonProperty("patient_prenom")
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    @JsonProperty("patient_adresse")
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    @JsonProperty("patient_telephone")
    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
        return "Patient{" +
                "adresse='" + adresse + '\'' +
                ", idPatient=" + idPatient +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
