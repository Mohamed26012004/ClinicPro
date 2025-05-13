package edu.ezip.ing1.pds;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import com.formdev.flatlaf.FlatLightLaf;
import edu.ezip.ing1.pds.business.dto.PlanificationExamen;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.*;
import edu.ezip.ing1.pds.services.planning.PlanificationService;
import edu.ezip.ing1.pds.services.planning.PlanificationWithNameService;


import javax.swing.*;

public class MainFrontEnd {


    public static void main(String[] args) throws IOException, InterruptedException {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        //final EquipementService equipementService = new EquipementService(networkConfig);
        final PlanificationService planificationService = new PlanificationService(networkConfig);
        final PlanificationWithNameService planificationWithNameService = new PlanificationWithNameService(networkConfig);



//        final PlanificationExamen plan = new PlanificationExamen(0, 3, 2, 2, LocalDate.now(), LocalTime.now(), LocalTime.now());
//        planificationService.insertPlanification(plan);
//        System.out.println(planificationWithNameService.selectPlanificationWithNames());
        // Pour un jolie look and feel.
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        Fenetre fene = new Fenetre();
        fene.setVisible(true);




    }
}




