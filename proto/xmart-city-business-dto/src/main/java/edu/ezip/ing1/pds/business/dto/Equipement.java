
package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonRootName(value = "equipement")
public class Equipement {
    private  int idEquipement;
    private  int coutEquipement;
    private  String nomEquipement;
    @JsonDeserialize(using = DeserialisationDate.class) // doit etre au dessus des dates
    private LocalDate dateEquipement;

    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Equipement() {
    }
    public final Equipement build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idEquipement", "coutEquipement","nomEquipement", "dateEquipement");
        return this;

    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idEquipement),String.valueOf(coutEquipement), nomEquipement, dateEquipement.format(formatDate));
    }

    public Equipement(int coutEquipement, String nomEquipement, LocalDate dateEquipement, int idEquipement) {
        this.nomEquipement = nomEquipement;
        this.coutEquipement = coutEquipement;
        this.dateEquipement = dateEquipement;
        this.idEquipement= idEquipement;
    }

    public int   getCoutEquipement() {
        return coutEquipement;
    }

    public int  getIdEquipement() {
        return idEquipement;
    }

    public String getNomEquipement() {
        return nomEquipement;
    }
    public LocalDate getDateEquipement(){
        return dateEquipement;
    }


    @JsonProperty("equipement_coutEquipement")
    public void setCoutEquipement(int coutEquipement) {
        this.coutEquipement = coutEquipement;
    }


    @JsonProperty("equipement_idEquipement")
    public void setIdEquipement(int idEquipement) {
        this.idEquipement = idEquipement;
    }

    @JsonProperty("equipement_nomEquipement")
    public void setNomEquipement(String nomEquipement) {
        this.nomEquipement = nomEquipement;
    }
    @JsonProperty("equipement_dateEquipement")
    public void setDateEquipement(LocalDate dateEquipement) {
        this.dateEquipement = dateEquipement;
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
                "coutEquipement='" + coutEquipement + '\'' +
                ", idEquipement='" + idEquipement + '\'' +
                ", nomEquipement='" + nomEquipement + '\'' +
                "dateEquipement='" + dateEquipement + '\'' +
                '}';
    }

}

