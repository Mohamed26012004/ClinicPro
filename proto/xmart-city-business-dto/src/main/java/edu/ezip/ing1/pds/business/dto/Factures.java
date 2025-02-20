package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.LinkedHashSet;
import java.util.Set;

public class Factures {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("factures")
    private  Set<Facture> factures = new LinkedHashSet<Facture>();

    public Set<Facture> getFactures() {
        return factures;
    }

    public void setFactures(Set<Facture> factures) {
        this.factures = factures;
    }

    public final Factures add (final Facture facture) {
        factures.add(facture);
        return this;
    }

    @Override
    public String toString() {
        return "Factures{" +
                "factures=" + factures +
                '}';

    }
}