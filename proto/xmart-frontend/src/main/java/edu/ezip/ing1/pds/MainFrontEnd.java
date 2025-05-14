package edu.ezip.ing1.pds;

import java.io.IOException;
import java.time.LocalDate;

import com.formdev.flatlaf.FlatLightLaf;

import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.*;
import edu.ezip.ing1.pds.services.FactureService;


import javax.swing.*;

public class MainFrontEnd {


    public static void main(String[] args) throws IOException, InterruptedException {
//        final String networkConfigFile = "network.yaml";
//        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
//        final EquipementService equipementService = new EquipementService(networkConfig);




        // Pour un jolie look and feel.
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        Fenetre fene = new Fenetre();
        fene.setVisible(true);

        FenetreMedecin fm = new FenetreMedecin();
        fm.setVisible(true);


    }
}
