package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Salles {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("salles")
    private Set<Salle> salles = new LinkedHashSet<Salle>();

    public Set<Salle> getStudents() {
        return salles;
    }

    public void setStudents(Set<Salle> salles) {
        this.salles = salles;
    }

    public final Salles add (final Salle salle) {
        salles.add(salle);
        return this;
    }

    @Override
    public String toString() {
        return "Salles{" +
                "salles=" + salles +
                '}';
    }
}
