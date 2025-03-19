package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class RendezVouss {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("rendezvouss")
    private Set<RendezVous> rdvs = new LinkedHashSet<RendezVous>();

    public final RendezVouss add (final RendezVous rdv) {
        rdvs.add(rdv);
        return this;
    }
    public Set<RendezVous> getRdvs() {
        return rdvs;
    }

    public void setRdvs(Set<RendezVous> rdvs) {
        this.rdvs = rdvs;
    }

    @Override
    public String toString() {
        return "RendezVouss{" +
                "rdvs=" + rdvs +
                '}';
    }
}
