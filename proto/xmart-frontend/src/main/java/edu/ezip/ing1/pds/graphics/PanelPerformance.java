package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.FactureService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanelPerformance extends JPanel {
    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final FactureService factureService = new FactureService(networkConfig);


    public PanelPerformance(){

        setLayout(new BorderLayout());
        JPanel pan = new JPanel(new BorderLayout());
        pan.setPreferredSize(new Dimension(800,800));
        JScrollPane scrollPane = new JScrollPane();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        try {
            Factures factures = factureService.facturepayees();
            for (Facture facture : factures.getFactures()){
                JLabel label = new JLabel(facture.toString());
                panel.add(label);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scrollPane = new JScrollPane(panel);
        pan.removeAll();
        pan.add(scrollPane, BorderLayout.CENTER);
        pan.repaint();
        pan.revalidate();
        add(pan, BorderLayout.CENTER);
    }
}
