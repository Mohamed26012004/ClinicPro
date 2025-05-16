package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TotalPaiements {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("totalPaiements")
    private List<TotalPaiement> totalPaiements = new ArrayList<>();

    // Méthode pour ajouter un TotalPaiement à la liste
    public final TotalPaiements add(final TotalPaiement totalPaiement) {
        totalPaiements.add(totalPaiement);
        return this;
    }

    // Getter pour récupérer la liste des TotalPaiement
    public List<TotalPaiement> getTotalPaiements() {
        return totalPaiements;
    }

    // Setter pour définir la liste des TotalPaiement
    public void setTotalPaiements(List<TotalPaiement> totalPaiements) {
        this.totalPaiements = totalPaiements;
    }

    @Override
    public String toString() {
        return "TotalPaiements{" +
                "totalPaiements=" + totalPaiements +
                '}';
    }
}
