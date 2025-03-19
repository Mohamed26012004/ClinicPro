package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Horaire;
import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.business.dto.Salles;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.servicesplanning.SalleService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static edu.ezip.ing1.pds.graphics.Fenetre.*;


public class PanelManipulationSalle extends JPanel {

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final static SalleService salleService = new SalleService(networkConfig);

    private static JPanel panelSalleDejaExistante = new JPanel();
    private static Salles salleDejaExistantes;


    static {
        try {
            salleDejaExistantes = salleService.selectSalles();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PanelManipulationSalle(){
        setLayout(new BorderLayout());
        add(SalleDejaExistantes(), BorderLayout.CENTER);
        add(boutons(), BorderLayout.NORTH);
    }

    public static JPanel SalleDejaExistantes(){

        panelSalleDejaExistante.setLayout(new BorderLayout());
        panelSalleDejaExistante.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(400, 400));
        for (Salle s : salleDejaExistantes.getSalles()){
            PanelSalle panelSalle = new PanelSalle(s);
            panel.add(panelSalle);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        panelSalleDejaExistante.add(scrollPane, BorderLayout.CENTER);
        panelSalleDejaExistante.repaint();
        panelSalleDejaExistante.revalidate();

        return panelSalleDejaExistante;
    }

    public JPanel boutons(){
        JPanel panel = new JPanel(new FlowLayout());
        JButton creer = new JButton("Créer");
        JButton modifier = new JButton("Modifier");
        JButton supprimer = new JButton("Supprimer");

        creer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameCreationSalle fen = new FrameCreationSalle();
                PanelManipulationSalle.this.removeAll();
                PanelManipulationSalle.this.add(SalleDejaExistantes(), BorderLayout.CENTER);
                PanelManipulationSalle.this.add(boutons(), BorderLayout.NORTH);
                PanelManipulationSalle.this.revalidate();
            }
        });

        modifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelSalle panelSalle = PanelSalle.panelSalleCliquer;
                FrameModificationSalle fen = new FrameModificationSalle(panelSalle.salleOfPanel());
                PanelManipulationSalle.this.removeAll();
                PanelManipulationSalle.this.add(SalleDejaExistantes(), BorderLayout.CENTER);
                PanelManipulationSalle.this.add(boutons(), BorderLayout.NORTH);
                PanelManipulationSalle.this.revalidate();

            }
        });
        supprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelSalle panelSalle = PanelSalle.panelSalleCliquer;
                Salle salle = panelSalle.salleOfPanel();
                try {
                    salleService.deleteSalle(salle);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
//                Fenetre.getContentPane().removeAll();
//                contentPane.add(new PanelManipulationSalle(), BorderLayout.CENTER);
//                contentPane.add(PanelGauche(), BorderLayout.WEST);
//                contentPane.repaint();
//                contentPane.revalidate();
//
            }
        });
        panel.add(creer);
        panel.add(modifier);
        panel.add(supprimer);
        return panel;
    }

}
