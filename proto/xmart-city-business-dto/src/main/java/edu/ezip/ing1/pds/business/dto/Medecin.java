package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonRootName(value = "medecin")
public class Medecin {
    private int numeroADELI;
    private  String nom;
    private  String prenom;
    private  String telephone;
    private  String specialite;
    private int salaire;

    public Medecin() {
    }
    public final Medecin build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "numeroADELI", "nom","prenom", "telephone", "specialite", "salaire");
        return this;
    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(numeroADELI), nom, prenom, telephone, specialite, String.valueOf(salaire) );
    }

    public Medecin(int numeroADELI, String nom, String prenom, String telephone, String specialite, int salaire) {
        this.numeroADELI = numeroADELI;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.specialite = specialite;
        this.salaire = salaire;
    }

    public int getNumeroADELI() {
        return numeroADELI;
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
    public String getSpecialite() {
        return specialite;
    }
    public int getSalaire() {
        return salaire;
    }

    @JsonProperty("medecin_numeroADELI")
    public void setNumeroADELI(int numeroADELI) {
        this.numeroADELI = numeroADELI;
    }

    @JsonProperty("medecin_nom")
    public void setNom(String nom) {
        this.nom = nom;
    }

    @JsonProperty("medecin_prenom")
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @JsonProperty("medecin_telephone")
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @JsonProperty("medecin_specialite")
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    @JsonProperty("medecin_salaire")
    public void setSalaire(int salaire) {
        this.salaire = salaire;
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
        return "Medecin{" +
                "numeroADELI=" + numeroADELI +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", specialite='" + specialite + '\'' +
                ", salaire=" + salaire +
                '}';
    }
}
