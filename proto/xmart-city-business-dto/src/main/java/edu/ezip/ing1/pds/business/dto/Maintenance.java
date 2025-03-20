
/*package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "maintenance")
public class Maintenance {
    private  String typeMaintenance;
    private  int coutMaintenance;
    private  int idMaintenance;
    private Date dateMaintenace;


    public Maintenance() {
    }
    public final Maintenance build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idMaintenance", "typeMaintenance","coutMaintenance", "dateMaintenance");
        return this;

    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idMaintenance),String.valueOf(coutMaintenance), typeMaintenance, dateMaintenace.toString());
    }

    public Maintenance(int coutMaintenance, String typeMaintenance, int idMaintenance, Date dateMaintenance) {
        this.nomEquipement = nomEquipement;
        this.coutEquipement = coutEquipement;
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
    public Date getDateEquipement(){
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
    public void setDateEquipement(Date dateEquipement) {
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

}*/

