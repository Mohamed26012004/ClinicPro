package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@JsonRootName(value = "planificationWithName")
public class PlanificationWithName {


    private int idPlanification;
    private String nomMedecin;
    private String prenomMedecin;
    private String nomPatient;
    private String prenomPatient;
    private String nomExamen;
    private String numeroSalle;
    @JsonDeserialize(using = DeserialisationDate.class)
    private LocalDate datePlanification;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureDebut;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureFin;
    private final DateTimeFormatter formatHeure = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PlanificationWithName(){};

    public final PlanificationWithName build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idPlanification", "nomMedecin", "prenomMedecin","nomPatient", "prenomPatient",
                "nomExamen", "numeroSalle","datePlanification", "heureDebut", "heureFin");
        return this;
    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idPlanification), nomMedecin, prenomMedecin,
                nomPatient, prenomPatient, nomExamen, numeroSalle,
                datePlanification.format(formatDate), heureDebut.format(formatHeure), heureFin.format(formatHeure));
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



    public LocalDate getDatePlanification() {
        return datePlanification;
    }
    @JsonProperty("planificationWithName_datePlanification")
    public void setDatePlanification(LocalDate datePlanification) {
        this.datePlanification = datePlanification;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }
    @JsonProperty("planificationWithName_heureDebut")
    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }
    @JsonProperty("planificationWithName_heureFin")
    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public int getIdPlanification() {
        return idPlanification;
    }
    @JsonProperty("planificationWithName_idPlanification")
    public void setIdPlanification(int idPlanification) {
        this.idPlanification = idPlanification;
    }

    public String getNomExamen() {
        return nomExamen;
    }
    @JsonProperty("planificationWithName_nomExamen")
    public void setNomExamen(String nomExamen) {
        this.nomExamen = nomExamen;
    }

    public String getNomMedecin() {
        return nomMedecin;
    }
    @JsonProperty("planificationWithName_nomMedecin")
    public void setNomMedecin(String nomMedecin) {
        this.nomMedecin = nomMedecin;
    }

    public String getNomPatient() {
        return nomPatient;
    }
    @JsonProperty("planificationWithName_nomPatient")
    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public String getNumeroSalle() {
        return numeroSalle;
    }
    @JsonProperty("planificationWithName_numeroSalle")
    public void setNumeroSalle(String numeroSalle) {
        this.numeroSalle = numeroSalle;
    }

    public String getPrenomMedecin() {
        return prenomMedecin;
    }
    @JsonProperty("planificationWithName_prenomMedecin")
    public void setPrenomMedecin(String prenomMedecin) {
        this.prenomMedecin = prenomMedecin;
    }

    public String getPrenomPatient() {
        return prenomPatient;
    }
    @JsonProperty("planificationWithName_prenomPatient")
    public void setPrenomPatient(String prenomPatient) {
        this.prenomPatient = prenomPatient;
    }

    @Override
    public String toString() {
        return "PlanificationWithName{" +
                "datePlanification=" + datePlanification +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", idPlanification=" + idPlanification +
                ", nomExamen='" + nomExamen +
                ", nomMedecin='" + nomMedecin +
                ", nomPatient='" + nomPatient +
                ", numeroSalle='" + numeroSalle +
                ", prenomMedecin='" + prenomMedecin +
                ", prenomPatient='" + prenomPatient +
                '}';
    }
}
