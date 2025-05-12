package edu.ezip.ing1.pds.business.dto;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonRootName;
@JsonRootName(value = "totalMaintenance")

public class TotalMaintenance {
//    @JsonDeserialize(using = DeserialisationDate2.class)
    private LocalDate dateMaintenance;
    private int totalMaintenance;
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TotalMaintenance() {}

    public TotalMaintenance(LocalDate dateMaintenance, int totalMaintenance) {
        this.dateMaintenance= dateMaintenance;
        this.totalMaintenance = totalMaintenance;
    }

    public LocalDate getDateMaintenance() {
        return dateMaintenance;
    }

    public void setDateMaintenance(LocalDate dateMaintenance) {
        this.dateMaintenance = dateMaintenance;
    }

    public double getTotalMaintenance() {
        return totalMaintenance;
    }

    public void setTotalMaintenance(int totalMaintenance) {
        this.totalMaintenance = totalMaintenance;
    }

    @Override
    public String toString() {
        return "TotalMaintenance{" +
                "dateMaintenance=" + dateMaintenance +
                ", totalMaintenance=" + totalMaintenance +
                '}';
    }
}


