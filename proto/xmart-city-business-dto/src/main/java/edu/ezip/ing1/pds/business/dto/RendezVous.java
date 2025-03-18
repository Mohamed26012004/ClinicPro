package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

@JsonRootName(value = "rendezvous")
public class RendezVous {

    private int idRendezVous;
    private int idPatient;
    private int id;
    private int numeroADELI;
    @JsonDeserialize(using = DeserialisationDate.class)
    private LocalDate dateRendezVous;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureDebut;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureFin;
    private final DateTimeFormatter formatHeure = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RendezVous(){
    }

    public final RendezVous build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "numeroADELI", "idPatient","id", "dateRendezVous", "heureDebut", "heureFin");
        return this;
    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(numeroADELI), String.valueOf(idPatient),
        String.valueOf(id), dateRendezVous.format(formatDate), heureDebut.format(formatHeure), heureFin.format(formatHeure));
    }
    public RendezVous(int numeroADELI, int idPatient, int id, LocalDate dateRendezVous, LocalTime heureDebut, LocalTime heureFin) {
        this.numeroADELI = numeroADELI;
        this.idPatient = idPatient;
        this.id = id;
        this.heureFin = heureFin;
        this.heureDebut = heureDebut;
        this.dateRendezVous = dateRendezVous;
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
    public LocalDate getDateRendezVous() {
        return dateRendezVous;
    }
    public LocalTime getHeureDebut() {
        return heureDebut;
    }
    public LocalTime getHeureFin() {
        return heureFin;
    }
    public int getId() {
        return id;
    }
    public int getIdPatient() {
        return idPatient;
    }
    public int getIdRendezVous() {
        return idRendezVous;
    }
    public int getNumeroADELI() {
        return numeroADELI;
    }

    @JsonProperty("rendezvous_dateRedezVous")
    public void setDateRendezVous(LocalDate dateRendezVous) {
        this.dateRendezVous = dateRendezVous;
    }
    @JsonProperty("rendezvous_heureDebut")
    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }
    @JsonProperty("rendezvous_heureFin")
    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }
    @JsonProperty("rendezvous_id")
    public void setId(int id) {
        this.id = id;
    }
    @JsonProperty("rendezvous_idPatient")
    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }
    @JsonProperty("rendezvous_idRendezVous")
    public void setIdRendezVous(int idRendezVous) {
        this.idRendezVous = idRendezVous;
    }
    @JsonProperty("rendezvous_numeroADELI")
    public void setNumeroADELI(int numeroADELI) {
        this.numeroADELI = numeroADELI;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "dateRendezVous=" + dateRendezVous +
                ", idRendezVous=" + idRendezVous +
                ", idPatient=" + idPatient +
                ", id=" + id +
                ", numeroADELI=" + numeroADELI +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", formatHeure=" + formatHeure +
                ", formatDate=" + formatDate +
                '}';
    }
}
