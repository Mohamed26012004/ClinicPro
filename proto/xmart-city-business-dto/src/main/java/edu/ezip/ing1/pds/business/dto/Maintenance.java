
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

@JsonRootName(value = "maintenance")
public class Maintenance {
    private  int idMaintenance;
    private  int coutMaintenance;
    private  String typeMaintenance;
    @JsonDeserialize(using = DeserialisationDate2.class) //
    private LocalDate dateMaintenance;

    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Maintenance() {
    }
    public final Maintenance build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "idMaintenance", "coutMaintenance","typeMaintenance", "dateMaintenance");
        return this;

    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(idMaintenance),String.valueOf(coutMaintenance), typeMaintenance, dateMaintenance.format(formatDate));
    }

    public Maintenance(int coutMaintenance, String typeMaintenance, LocalDate dateMaintenance, int idMaintenance) {
        this.typeMaintenance = typeMaintenance;
        this.coutMaintenance = coutMaintenance;
        this.dateMaintenance = dateMaintenance;
        this.idMaintenance= idMaintenance;
    }

    public int   getCoutMaintenance() {
        return coutMaintenance;
    }

    public int  getIdMaintenance() {
        return idMaintenance;
    }

    public String getTypeMaintenance() {
        return typeMaintenance;
    }
    public LocalDate getDateMaintenance(){
        return dateMaintenance;
    }


    @JsonProperty("maintenance_coutMaintenance")
    public void setCoutMaintenance(int coutMaintenance) {
        this.coutMaintenance = coutMaintenance;
    }


    @JsonProperty("maintenance_idMaintenance")
    public void setIdMaintenance(int idMaintenance) {
        this.idMaintenance = idMaintenance;
    }

    @JsonProperty("maintenance_typeMaintenance")
    public void setTypeMaintenance(String typeMaintenance) {
        this.typeMaintenance = typeMaintenance;
    }
    @JsonProperty("maintenance_dateMaintenance")
    public void setDateMaintenance(LocalDate dateMaintenance) {
        this.dateMaintenance = dateMaintenance;
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
        return "maintenance{" +
                "coutMaintenance='" + coutMaintenance + '\'' +
                ", idMaintenance='" + idMaintenance + '\'' +
                ", nomMaintenance='" + typeMaintenance + '\'' +
                "dateMaintenance='" + dateMaintenance + '\'' +
                '}';
    }

}

