package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Horaires {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("horaires")
    private Set<Horaire> horaires = new LinkedHashSet<Horaire>();

    public final Horaires add (final Horaire h) {
        horaires.add(h);
        return this;
    }

    public Set<Horaire> getHoraires() {
        return horaires;
    }

    public void setHoraires(Set<Horaire> horaires) {
        this.horaires = horaires;
    }

    @Override
    public String toString() {
        return "Horaires{" +
                "horaires=" + horaires +
                '}';
    }
}
