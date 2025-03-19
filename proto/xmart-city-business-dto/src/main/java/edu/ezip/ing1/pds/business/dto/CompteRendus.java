package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.LinkedHashSet;
import java.util.Set;

public class CompteRendus {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("compteRendus")
    private Set<CompteRendu> compteRendus = new LinkedHashSet<CompteRendu>();

    public Set<CompteRendu> getCompteRendus() {
        return compteRendus;
    }

    public void setCompteRendus(Set<CompteRendu> compteRendus) {this.compteRendus = compteRendus;}

    public final CompteRendus add (final CompteRendu compteRendu) {
        compteRendus.add(compteRendu);
        return this;
    }

    @Override
    public String toString() {
        return "compteRendus{" +
                "compteRendus=" + compteRendus +
                '}';

    }
}