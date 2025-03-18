package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.LinkedHashSet;
import java.util.Set;

public class Traitements {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Traitements")
    private Set<Traitement> Traitements = new LinkedHashSet<Traitement>();

    public Set<Traitement> getTraitements() {
        return Traitements;
    }

    public void setTraitements(Set<Traitement> Traitements) {this.Traitements = Traitements;}

    public final Traitements add (final Traitement Traitement) {
        Traitements.add(Traitement);
        return this;
    }

    @Override
    public String toString() {
        return "Traitements{" +
                "Traitements=" + Traitements +
                '}';

    }
}