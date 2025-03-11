package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Patients {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("patients")
    private Set<Patient> patients = new LinkedHashSet<Patient>();

    public final Patients add (final Patient p) {
        patients.add(p);
        return this;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public String toString() {
        return "Patients{" +
                "patients=" + patients +
                '}';
    }
}
