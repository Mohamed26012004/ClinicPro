package edu.ezip.ing1.pds.business.dto;
 
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
 
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
 
 
@JsonRootName(value = "facture")
public class Facture {
    private int idFacture;
    private  double montantFacture;
    private boolean regle;
    private int idPatient;
    private int idExamen;  
   
 
    @JsonDeserialize(using = DeserialisationDate.class) // doit etre au dessus des dates
    private LocalDate dateFacture;
 
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 
 
    public Facture() {
    }
    public final Facture build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idFacture","dateFacture", "montantFacture", "regle", "idPatient", "idExamen");
        return this;
 
    }
 
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
                return buildPreparedStatement(preparedStatement, String.valueOf(idFacture), String.valueOf(montantFacture), dateFacture.format(formatDate), String.valueOf(regle), String.valueOf(idPatient), String.valueOf(idExamen));
    }
 
    public Facture(double montantFacture, boolean regle, LocalDate dateFacture, int idPatient, int idExamen) {
        this.montantFacture = montantFacture;
        this.regle = regle;
        this.dateFacture = dateFacture;
        this.idPatient = idPatient;
        this.idExamen = idExamen;
    }
 
    public double getMontantFacture() {
        return montantFacture;
    }
 
    public boolean  getRegle() {
        return regle;
    }
 
    public int  getIdFacture() {
        return idFacture;
    }
 
 
    public LocalDate getDateFacture() {
        return dateFacture;
    }
 
    public int getIdPatient() {
        return idPatient;
    }
 
    public int getIdExamen() {
        return idExamen;
    }
 
    @JsonProperty("facture_idFacture")
    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }
 
    @JsonProperty("facture_montant")
    public void setMontantFacture(double montantFacture) {
        this.montantFacture = montantFacture;
    }
 
    @JsonProperty("facture_regle")
    public void setRegle(boolean regle) {
        this.regle = regle;
    }
 
    @JsonProperty("facture_date")
    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }
 
    @JsonProperty("facture_idPatient")
    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }
 
    @JsonProperty("facture_idExamen")
    public void setIdExamen(int idExamen) {
        this.idExamen = idExamen;
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
        return "facture{" +
    "facture_date='" + dateFacture + '\'' +
    ", facture_idFacture=" + idFacture +
    ", facture_regle=" + regle +
    ", facture_montant=" + montantFacture +
    ", facture_idPatient=" + idPatient +
    ", facture_idExamen=" + idExamen +
    '}';
 
    }
}