package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Medecins {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("medecins")
    private Set<Medecin> medecins = new LinkedHashSet<Medecin>();

    public Set<Medecin> getMedecins() {
        return medecins;
    }

    public void setMedecins(Set<Medecin> medecins) {
        this.medecins = medecins;
    }
    public final Medecins add (final Medecin medecin) {
        medecins.add(medecin);
        return this;
    }

    @Override
    public String toString() {
        return "Medecins{" +
                "medecins=" + medecins +
                '}';
    }
}
