package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonRootName(value = "examen")
public class Examen {

    private  String nom;
    private  double cout;
    private  int id;
    @JsonDeserialize(using = ClasseDeDeserialisation.class)
    private LocalTime duree;
    private DateTimeFormatter formattage = DateTimeFormatter.ofPattern("HH:mm");

    public Examen(){
    }

    public final Examen build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "id", "nom", "cout","duree");
        return this;
    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
        throws SQLException, NoSuchFieldException, IllegalAccessException {
    return buildPreparedStatement(preparedStatement, nom, String.valueOf(cout), duree.format(formattage));
    }

    public Examen(String nom, double cout,LocalTime duree) {
        this.cout = cout;
        this.nom = nom;
        this.duree = duree;
    }

    public String getNom() {
        return nom;
    }

    @JsonProperty("examen_nom")
    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getCout() {
        return cout;
    }

    @JsonProperty("examen_cout")
    public void setCout(double cout) {
        this.cout = cout;
    }

    public int getId() {
        return id;
    }

    @JsonProperty("examen_id")
    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getDuree() {
        return duree;
    }

    @JsonProperty("examen_duree")
    public void setDuree(LocalTime duree) {
        this.duree = duree;
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
        return "Examen{" +
                "cout=" + cout +
                ", duree=" + duree +
                ", id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}
