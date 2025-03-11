
package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "equipement")
public class Equipement {
    private  int idEquipement;
    private  int cout;
    private  String nom;


    public Equipement() {
    }
    public final Equipement build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idEquipement", "cout","nom");
        return this;

    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idEquipement),String.valueOf(cout), nom);
    }

    public Equipement(int cout, String nom) {
        this.nom = nom;
        this.cout = cout;
    }

    public int   getCout() {
        return cout;
    }

    public int  getIdEquipement() {
        return idEquipement;
    }

    public String getNom() {
        return nom;
    }


    @JsonProperty("equipement_cout")
    public void setDateFacture(int cout) {
        this.cout = cout;
    }


    @JsonProperty("equipement_idEquipement")
    public void setIdFacture(int idEquipement) {
        this.idEquipement = idEquipement;
    }

    @JsonProperty("equipement_nom")
    public void setRegle(String nom) {
        this.nom = nom;
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
        return "equipement{" +
                "cout='" + cout + '\'' +
                ", idEquipement='" + idEquipement + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }

}

