package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "examen")
public class Examen {

    private  String nom;
    private  double cout;
    private  int id;
    private String numeroSalle;

    public Examen(){
    }

    public final Examen build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "nom", "cout","numeroSalle");
    return this;
}

public final PreparedStatement build(PreparedStatement preparedStatement)
    throws SQLException, NoSuchFieldException, IllegalAccessException {
return buildPreparedStatement(preparedStatement, nom, String.valueOf(cout), numeroSalle);
}

    public Examen(String nom, double cout, String numeroSalle) {
        this.cout = cout;
        this.nom = nom;
        this.numeroSalle = numeroSalle;
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

    public String getNumeroSalle() {
        return numeroSalle;
    }

    @JsonProperty("examen_numerp_salle")
    public void setNumeroSalle(String numeroSalle) {
        this.numeroSalle = numeroSalle;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Examen{");
        sb.append("nom=").append(nom);
        sb.append(", cout=").append(cout);
        sb.append(", id=").append(id);
        sb.append(", numeroSalle=").append(numeroSalle);
        sb.append('}');
        return sb.toString();
    }

    

}
