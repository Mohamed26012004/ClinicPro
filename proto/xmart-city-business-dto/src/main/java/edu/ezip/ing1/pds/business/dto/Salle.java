package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "salle")
public class Salle {

    private int id;
    private String numeroSalle;
    private String typeSalle;
    private String statut;

    public Salle(){
    }
    public final Salle build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "id", "numeroSalle", "typeSalle","statut");
        return this;
    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(id), numeroSalle, typeSalle, statut);
    }
    public Salle(String numeroSalle, String typeSalle, String statut){
        this.numeroSalle = numeroSalle;
        this.typeSalle = typeSalle;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }
    public String getNumeroSalle() {
        return numeroSalle;
    }
    public String getStatut() {
        return statut;
    }
    public String getTypeSalle() {
        return typeSalle;
    }

    @JsonProperty("salle_id")
    public void setId(int id) {
        this.id = id;
    }
    @JsonProperty("salle_numeroSalle")
    public void setNumeroSalle(String numeroSalle) {
        this.numeroSalle = numeroSalle;
    }

    @JsonProperty("salle_typeSalle")
    public void setTypeSalle(String typeSalle) {
        this.typeSalle = typeSalle;
    }
    @JsonProperty("salle_statut")
    public void setStatut(String statut) {
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

    @Override
    public String toString() {
        return "Salle{" +
                "id=" + id +
                ", numeroSalle='" + numeroSalle + '\'' +
                ", typeSalle='" + typeSalle + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}
