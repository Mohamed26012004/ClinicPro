package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class PlanificationWithNames {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("planificationWithNames")
    private Set<PlanificationWithName> planificationWithNames = new LinkedHashSet<PlanificationWithName>();

    public final PlanificationWithNames add (final PlanificationWithName plan) {
        planificationWithNames.add(plan);
        return this;
    }

    public Set<PlanificationWithName> getPlanificationWithNames() {
        return planificationWithNames;
    }

    public void setPlanificationWithNames(Set<PlanificationWithName> planificationWithNames) {
        this.planificationWithNames = planificationWithNames;
    }

    @Override
    public String toString() {
        return "PlanificationWithNames{" +
                "planificationWithNames=" + planificationWithNames +
                '}';
    }
}
