package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Creneaux {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("creneaux")
    private Set<Creneau> creneaux = new LinkedHashSet<Creneau>();

    public final Creneaux add (final Creneau c) {
        creneaux.add(c);
        return this;
    }

    public Set<Creneau> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(Set<Creneau> creneaux) {
        this.creneaux = creneaux;
    }

    @Override
    public String toString() {
        return "Creneaux{" +
                "creneaux=" + creneaux +
                '}';
    }
}
