package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesplanning.SalleService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class PanelManipulationSalle extends JPanel {

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final static SalleService salleService = new SalleService(networkConfig);

    private static JButton ajouter;
    private static JButton modifier;
    private static JButton supprimer;
    private static JScrollPane scrollPane;
    private static JPanel panneau = new JPanel(new BorderLayout());


    public PanelManipulationSalle() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(500, 300));
        try {
            for(Salle salle : salleService.selectSalles().getSalles()){
                PanelSalle pan = new PanelSalle(salle);
                add(pan);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static JPanel afficheSalle() {
        PanelManipulationSalle panel = new PanelManipulationSalle();
        scrollPane = new JScrollPane(panel);

        panneau.removeAll();
        panneau.add(scrollPane, BorderLayout.CENTER);

        panneau.add(boutons(), BorderLayout.NORTH);

        panneau.revalidate();
        panneau.repaint();

        return panneau;
    }

    public static JPanel boutons(){
        JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ajouter = new JButton("Ajouter");
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameCreationSalle fen = new FrameCreationSalle(null);
            }
        });
        pane.add(ajouter);

        modifier = new JButton("Modifier");
        modifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelSalle p = PanelSalle.panelSalleCliquer;
                Salle salle = p.salleOfPanel();
                p.setBackground(null);
                FrameCreationSalle fen = new FrameCreationSalle(salle);
            }
        });
        pane.add(modifier);
        supprimer = new JButton("Supprimer");
        supprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelSalle p = PanelSalle.panelSalleCliquer;
                Salle salle = p.salleOfPanel();
                p.setBackground(null);

                final String networkConfigFile = "network.yaml";
                final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
                final SalleService service = new SalleService(networkConfig);
                try {
                   service.deleteSalle(salle);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                PanelManipulationSalle.afficheSalle().revalidate();
                PanelManipulationSalle.afficheSalle().repaint();
            }
        });
        pane.add(supprimer);

        return pane;
    }

}

