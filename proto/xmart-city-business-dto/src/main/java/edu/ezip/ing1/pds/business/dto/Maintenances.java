package edu.ezip.ing1.pds.business.dto;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Maintenances {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("maintenances")
    private  Set<Maintenance> maintenances = new LinkedHashSet<Maintenance>();



    public final Maintenances add (final Maintenance maintenance) {
        maintenances.add(maintenance);
        return this;
    }

    public Set<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(Set<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Maintenances{");
        sb.append("maintenances=").append(maintenances);
        sb.append('}');
        return sb.toString();
    }



}

