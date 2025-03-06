package edu.ezip.ing1.pds.business.dto;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Paiements {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("paiements")
    private  Set<Paiement> paiements = new LinkedHashSet<Paiement>();

    public Set<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiments(Set<Paiement> paiements) {
        this.paiements = paiements;
    }

    public final Paiements add (final Paiement paiement) {
        paiements.add(paiement);
        return this;
    }

    @Override
    public String toString() {
        return "Paiements{" +
                "paiements=" + paiements +
                '}';

    }
}