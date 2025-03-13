package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.LinkedHashSet;
import java.util.Set;

public class compteRendus {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("compteRendus")
    private Set<compteRendu> compteRendus = new LinkedHashSet<compteRendu>();

    public Set<compteRendu> getcompteRendus() {
        return compteRendus;
    }

    public void setcompteRendus(Set<compteRendu> compteRendus) {this.compteRendus = compteRendus;}

    public final compteRendus add (final compteRendu compteRendu) {
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