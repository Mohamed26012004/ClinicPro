package edu.ezip.ing1.pds.business.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.ezip.ing1.pds.business.dto.TotalCout;
import java.util.ArrayList;
import java.util.List;



import java.util.LinkedHashSet;
import java.util.Set;

public class TotalCouts {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("totalCouts")
    private List<TotalCout> totalCouts = new ArrayList<>();  // Utilisation de List plutôt que Set

    // Méthode pour ajouter un TotalCout à la liste
    public final TotalCouts add(final TotalCout totalCout) {
        totalCouts.add(totalCout);  // Ajout à la liste
        return this;
    }

    // Getter pour récupérer la liste des TotalCout
    public List<TotalCout> getTotalCouts() {
        return totalCouts;
    }

    // Setter pour définir la liste des TotalCout
    public void setTotalCouts(List<TotalCout> totalCouts) {
        this.totalCouts = totalCouts;
    }

    @Override
    public String toString() {
        return "TotalCouts{" +
                "totalCouts=" + totalCouts +
                '}';
    }
}