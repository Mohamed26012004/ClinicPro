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

@JsonRootName(value = "planification")
public class PlanificationExamen {

    private int idPlanification;
    private int numeroADELI;
    private int idPatient;
    private int idExamen;
    private int idSalle;
    @JsonDeserialize(using = DeserialisationDate.class)
    private LocalDate datePlanification;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureDebut;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureFin;
    private final DateTimeFormatter formatHeure = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public PlanificationExamen(){}

    public PlanificationExamen(int numeroADELI, int idPatient,  int idExamen, int idSalle, LocalDate datePlanification, LocalTime heureFin, LocalTime heureDebut) {
        this.numeroADELI = numeroADELI;
        this.idSalle = idSalle;
        this.idPatient = idPatient;
        this.idExamen = idExamen;
        this.heureFin = heureFin;
        this.heureDebut = heureDebut;
        this.datePlanification = datePlanification;
    }

    public final PlanificationExamen build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idPlanification", "numeroADELI", "idPatient","idExamen", "idSalle",
                "datePlanification", "heureDebut", "heureFin");
        return this;
    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idPlanification), String.valueOf(numeroADELI), String.valueOf(idPatient),
                String.valueOf(idExamen), String.valueOf(idSalle), datePlanification.format(formatDate), heureDebut.format(formatHeure), heureFin.format(formatHeure));
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


    public int getNumeroADELI() {
        return numeroADELI;
    }
    @JsonProperty("planification_numeroADELI")
    public void setNumeroADELI(int numeroADELI) {
        this.numeroADELI = numeroADELI;
    }

    public int getIdSalle() {
        return idSalle;
    }
    @JsonProperty("planification_idSalle")
    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public int getIdPlanification() {
        return idPlanification;
    }
    @JsonProperty("planification_idPlanification")
    public void setIdPlanification(int idPlanification) {
        this.idPlanification = idPlanification;
    }

    public int getIdPatient() {
        return idPatient;
    }
    @JsonProperty("planification_idPatient")
    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getIdExamen() {
        return idExamen;
    }
    @JsonProperty("planification_idExamen")
    public void setIdExamen(int idExamen) {
        this.idExamen = idExamen;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }
    @JsonProperty("planification_heureDebut")
    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }
    @JsonProperty("planification_heureFin")
    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalDate getDatePlanification() {
        return datePlanification;
    }
    @JsonProperty("planification_datePlanification")
    public void setDatePlanification(LocalDate datePlanification) {
        this.datePlanification = datePlanification;
    }

    @Override
    public String toString() {
        return "PlanificationExamen{" +
                "idPlanification=" + idPlanification +
                ", numeroADELI=" + numeroADELI +
                ", idPatient=" + idPatient +
                ", idExamen=" + idExamen +
                ", idSalle=" + idSalle +
                ", datePlanification=" + datePlanification +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                '}';
    }
}
