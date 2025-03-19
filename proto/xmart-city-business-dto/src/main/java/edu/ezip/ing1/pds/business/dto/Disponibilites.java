package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Disponibilites {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("disponibilites")
    private Set<Disponibilite> disponibilites = new LinkedHashSet<Disponibilite>();


    public final Disponibilites add (final Disponibilite dispo) {
        disponibilites.add(dispo);
        return this;
    }

    public Set<Disponibilite> getDisponibilites() {
        return disponibilites;
    }

    public void setDisponibilites(Set<Disponibilite> disponibilites) {
        this.disponibilites = disponibilites;
    }

    @Override
    public String toString() {
        return "Disponibilites{" +
                "disponibilites=" + disponibilites +
                '}';
    }
}
