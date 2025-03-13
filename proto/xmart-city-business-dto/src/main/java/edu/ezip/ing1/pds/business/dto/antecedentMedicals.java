package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.LinkedHashSet;
import java.util.Set;

public class antecedentMedicals {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("antecedentMedicals")
    private Set<antecedentMedical> antecedentMedicals = new LinkedHashSet<antecedentMedical>();

    public Set<antecedentMedical> getAntecedentMedicals() {
        return antecedentMedicals;
    }

    public void setAntecedentMedicals(Set<antecedentMedical> antecedentMedicals) {this.antecedentMedicals = antecedentMedicals;}

    public final antecedentMedicals add (final antecedentMedical antecedentMedical) {
        antecedentMedicals.add(antecedentMedical);
        return this;
    }

    @Override
    public String toString() {
        return "antecedentMedicals{" +
                "antecedentMedicals=" + antecedentMedicals +
                '}';

    }
}