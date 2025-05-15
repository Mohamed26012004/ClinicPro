package edu.ezip.ing1.pds.business.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonRootName(value = "totalPaiement")
public class TotalPaiement {

    @JsonDeserialize(using = DeserialisationDate.class)
    private LocalDate datePaiement;

    private double totalPaiement;

    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TotalPaiement() {}

    public TotalPaiement(LocalDate datePaiement, double totalPaiement) {
        this.datePaiement = datePaiement;
        this.totalPaiement = totalPaiement;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public double getTotalPaiement() {
        return totalPaiement;
    }

    public void setTotalPaiement(double totalPaiement) {
        this.totalPaiement = totalPaiement;
    }

    @Override
    public String toString() {
        return "TotalPaiement{" +
                "datePaiement=" + datePaiement +
                ", totalPaiement=" + totalPaiement +
                '}';
    }
}
