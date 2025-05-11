package edu.ezip.ing1.pds.business.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.ezip.ing1.pds.business.dto.TotalCout;
import java.util.ArrayList;
import java.util.List;



import java.util.LinkedHashSet;
import java.util.Set;


public class TotalMaintenances {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("totalMaintenances")
    private List<TotalMaintenance> totalMaintenances = new ArrayList<>();  // Utilisation de List plutôt que Set

    // Méthode pour ajouter un Totalaintenance à la liste
    public final TotalMaintenances add(final TotalMaintenance totalMaintenance) {
        totalMaintenances.add(totalMaintenance);  // Ajout à la liste
        return this;
    }

    // Getter pour récupérer la liste des TotalMaintenance
    public List<TotalMaintenance> getTotalMaintenances() {
        return totalMaintenances;
    }

    // Setter pour définir la liste des TotalMaintenance
    public void setTotalMaintenances(List<TotalMaintenance> totalMaintenances) {
        this.totalMaintenances = totalMaintenances;
    }

    @Override
    public String toString() {
        return "TotalMaintenances{" +
                "totalMaintenances=" + totalMaintenances +
                '}';
    }
}

