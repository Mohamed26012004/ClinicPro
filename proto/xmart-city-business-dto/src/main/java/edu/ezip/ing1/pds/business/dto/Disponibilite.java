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

@JsonRootName(value = "disponibilite")
public class Disponibilite {

    private int idDisponibilite;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureDebut;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureFin;
    @JsonDeserialize(using = DeserialisationDate.class)
    private LocalDate date;
    private String statut;
    private int numeroADELI;
    private final DateTimeFormatter formatHeure = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Disponibilite(){}

    public final Disponibilite build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idDisponibilite", "numéroADELI", "date", "heureDebut", "heureFin", "status");
        return this;
    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idDisponibilite), String.valueOf(numeroADELI),
                date.format(formatDate), heureDebut.format(formatHeure), heureFin.format(formatHeure), statut);
    }
    public Disponibilite(int numeroADELI, LocalDate date, LocalTime heureDebut, LocalTime heureFin, String statut) {
        this.numeroADELI = numeroADELI;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.statut = statut;
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
    @JsonProperty("disponibilite_numeroADELI")
    public void setNumeroADELI(int numeroADELI) {
        this.numeroADELI = numeroADELI;
    }

    public LocalDate getDate() {
        return date;
    }
    @JsonProperty("disponibilite_date")
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }
    @JsonProperty("disponibilite_heureDebut")
    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }
    @JsonProperty("disponibilite_heureFin")
    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public int getIdDisponibilite() {
        return idDisponibilite;
    }
    @JsonProperty("disponibilite_idDisponibilite")
    public void setIdDisponibilite(int idDisponibilite) {
        this.idDisponibilite = idDisponibilite;
    }

    public String getStatut() {
        return statut;
    }
    @JsonProperty("disponibilite_statut")
    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Disponibilite{" +
                "idDisponibilite=" + idDisponibilite +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", statut='" + statut + '\'' +
                ", numeroADELI=" + numeroADELI +
                '}';
    }
}
