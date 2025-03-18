package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "horaire")
public class Horaire {

    private int id;
    private String jour;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureDebut;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime heureFin;
    private DateTimeFormatter formattage = DateTimeFormatter.ofPattern("HH:mm");

    public Horaire() {
    }

    public final Horaire build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "id", "jour","heureDebut", "heureFin");
        return this;
    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(id), jour, heureDebut.format(formattage), heureFin.format(formattage));
    }

    public Horaire(String jour, LocalTime heureDebut, LocalTime heureFin){
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
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

    public int getId() {
        return id;
    }
    public String getJour() {
        return jour;
    }
    public LocalTime getHeureDebut() {
        return heureDebut;
    }
    public LocalTime getHeureFin() {
        return heureFin;
    }

    @JsonProperty("horaire_id")
    public void setId(int id) {
        this.id = id;
    }
    @JsonProperty("horaire_jour")
    public void setJour(String jour) {
        this.jour = jour;
    }
    @JsonProperty("horaire_heureDebut")
    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }
    @JsonProperty("horaire_heureFin")
    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    @Override
    public String toString() {
        return "Horaire{" +
                ", id=" + id +
                ", jour='" + jour + '\'' +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                '}';
    }
}
