package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.LinkedHashSet;
import java.util.Set;

public class AntecedentMedicals {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("AntecedentMedicals")
    private Set<AntecedentMedical> AntecedentMedicals = new LinkedHashSet<AntecedentMedical>();

    public Set<AntecedentMedical> getAntecedentMedicals() {
        return AntecedentMedicals;
    }

    public void setAntecedentMedicals(Set<AntecedentMedical> AntecedentMedicals) {this.AntecedentMedicals = AntecedentMedicals;}

    public final edu.ezip.ing1.pds.business.dto.AntecedentMedicals add (final AntecedentMedical antecedentMedical) {
        AntecedentMedicals.add(antecedentMedical);
        return this;
    }

    @Override
    public String toString() {
        return "AntecedentMedicals{" +
                "AntecedentMedicals=" + AntecedentMedicals +
                '}';

    }
}