package edu.ezip.ing1.pds.business.dto;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Equipements {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("equipements")
    private  Set<Equipement> equipements = new LinkedHashSet<Equipement>();

    

    public final Equipements add (final Equipement equipement) {
        equipements.add(equipement);
        return this;
    }

    public Set<Equipement> getEquipements() {
        return equipements;
    }

    public void setEquipements(Set<Equipement> equipements) {
        this.equipements = equipements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Equipements{");
        sb.append("equipements=").append(equipements);
        sb.append('}');
        return sb.toString();
    }



}
