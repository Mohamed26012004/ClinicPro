
package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "Traitement")
public class Traitement {
    private  int id_Traitement;
    private  String type_Traitement;
    private  String description_Traitement;
    private  String debut_Traitement;
    private  String fin_Traitement;



    public Traitement() {
    }

    public final Traitement build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "id_Traitement", "type_Traitement","description_Traitement", "debut_traitement", "fin_Traitement");
        return this;

    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(id_Traitement),type_Traitement, description_Traitement, debut_Traitement, fin_Traitement);
    }

    public Traitement(String type_Traitement, String description_Traitement, String debut_Traitement, String fin_Traitement) {
        this.type_Traitement = type_Traitement;
        this.description_Traitement = description_Traitement;
        this.debut_Traitement = debut_Traitement;
        this.fin_Traitement = fin_Traitement;
    }


    public int getId_Traitement() {return id_Traitement;}
    public String getType_Traitement() {
        return type_Traitement;
    }
    public String getDescription_Traitement() {return description_Traitement;}
    public String getDebut_Traitement() {return debut_Traitement;}
    public String getFin_Traitement() {return fin_Traitement;}


    @JsonProperty("Traitement_type_Traitement")
    public void setType_Traitement(String type_Traitement) {this.type_Traitement = type_Traitement;}
    @JsonProperty("Traitement_id_Traitement")
    public void setId_Traitement(int id_Traitement) {
        this.id_Traitement = id_Traitement;
    }
    @JsonProperty("Traitement_description_Traitement")
    public void setDescription_Traitement(String description_Traitement) {this.description_Traitement = description_Traitement;}

    @JsonProperty("Traitement_debut_Traitement")
    public void setDebut_Traitement(String debut_Traitement) {this.debut_Traitement = debut_Traitement;}
    @JsonProperty("Traitement_fin_Traitement")
    public void setFin_Traitement(String fin_Traitement) {this.fin_Traitement = fin_Traitement;}

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
        return "Traitement{" +
                "id_Traitement='" + id_Traitement + '\'' +
                ", type_Traitement='" + type_Traitement + '\'' +
                ", description_Traitement='" + description_Traitement + '\'' +
                ", debut_Traitement='" + debut_Traitement + '\'' +
                ", fin_Traitement='" + fin_Traitement + '\'' +
                '}';
    }

}

