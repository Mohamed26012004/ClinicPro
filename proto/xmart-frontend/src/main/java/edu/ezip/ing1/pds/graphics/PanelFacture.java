package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.FactureService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;

public class PanelFacture extends JPanel{

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final FactureService factureService = new FactureService(networkConfig);

    private static JButton ajouter;
    private JPanel panneau= new JPanel(new FlowLayout());
    public PanelFacture(){

        setLayout(new BorderLayout());
        JPanel pan = new JPanel(new BorderLayout());
        pan.setPreferredSize(new Dimension(800,800));
        JScrollPane scrollPane = new JScrollPane();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        try {
            Factures factures = factureService.selectFactures();
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
//        this.repaint();
//        this.validate();

        ajouter = new JButton("Ajouter");
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean regle = true;
                int k=0;
                if (k%2 == 0){
                    regle = true;
                }else {
                    regle = false;
                }
                k++;
//                Date date = new Date();
//                Facture facture = new Facture(regle, date);

                //pan.removeAll();
//                try {
////                    factureService.insertFacture(facture);
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }

            }
        });
        panneau.add(ajouter);
        add(panneau, BorderLayout.SOUTH);
        repaint();
        revalidate();

    }
}
