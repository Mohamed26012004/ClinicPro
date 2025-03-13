package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.LinkedHashSet;
import java.util.Set;

public class Diagnostics {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Diagnostics")
    private Set<Diagnostic> Diagnostics = new LinkedHashSet<Diagnostic>();

    public Set<Diagnostic> getDiagnostics() {
        return Diagnostics;
    }

    public void setDiagnostics(Set<Diagnostic> Diagnostics) {this.Diagnostics = Diagnostics;}

    public final Diagnostics add (final Diagnostic Diagnostic) {
        Diagnostics.add(Diagnostic);
        return this;
    }

    @Override
    public String toString() {
        return "Diagnostics{" +
                "Diagnostics=" + Diagnostics +
                '}';

    }
}