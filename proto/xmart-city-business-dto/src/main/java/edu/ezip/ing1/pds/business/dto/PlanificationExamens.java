package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class PlanificationExamens {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("planifications")
    private Set<PlanificationExamen> planifications = new LinkedHashSet<PlanificationExamen>();

    public final PlanificationExamens add (final PlanificationExamen plan) {
        planifications.add(plan);
        return this;
    }

    public Set<PlanificationExamen> getPlanifications() {
        return planifications;
    }

    public void setPlanifications(Set<PlanificationExamen> planifications) {
        this.planifications = planifications;
    }

    @Override
    public String toString() {
        return "PlanificationExamens{" +
                "planifications=" + planifications +
                '}';
    }
}
