package edu.ezip.ing1.pds;
import java.io.IOException;

import com.formdev.flatlaf.FlatLightLaf;
import edu.ezip.ing1.pds.graphics.*;


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


    }
}
