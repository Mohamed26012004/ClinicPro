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

@JsonRootName(value = "totalCout")
public class TotalCout {
    @JsonDeserialize(using = DeserialisationDate.class)
    private LocalDate dateAchat;
    private int totalCout;
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TotalCout() {}

    public TotalCout(LocalDate dateAchat, int totalCout) {
        this.dateAchat = dateAchat;
        this.totalCout = totalCout;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat = dateAchat;
    }

    public double getTotalCout() {
        return totalCout;
    }

    public void setTotalCout(int totalCout) {
        this.totalCout = totalCout;
    }

    @Override
    public String toString() {
        return "TotalCout{" +
                "dateAchat=" + dateAchat +
                ", totalCout=" + totalCout +
                '}';
    }
}
