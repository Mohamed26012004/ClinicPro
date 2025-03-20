package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ExamenService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PanelManipulationExamen extends JPanel {

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final static ExamenService examenService = new ExamenService(networkConfig);

    private static JButton ajouter;
    private static JButton modifier;
    private static JButton supprimer;
    private static JScrollPane scrollPane;
    private static JPanel panneau = new JPanel(new BorderLayout());


    public PanelManipulationExamen(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(500, 300));
        setPreferredSize(new Dimension(800, 800));
        try {
            //examens = examenService.selectExamens();
            for(Examen exam : examenService.selectExamens().getExamens()){
                PanelExamen pan = new PanelExamen(exam);
                add(pan);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static JPanel afficheExamens() {
        PanelManipulationExamen panel = new  PanelManipulationExamen();
        scrollPane = new JScrollPane(panel);

        panneau.removeAll();
        panneau.add(scrollPane, BorderLayout.CENTER);

        JPanel titre = new JPanel(new FlowLayout());

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
                FrameCreationExamen fen = new FrameCreationExamen(null);
            }
        });
        pane.add(ajouter);

        modifier = new JButton("Modifier");
        modifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Examen examen = new Examen();
                PanelExamen p = PanelExamen.panelExamenCliquer;
                p.setBackground(null);
                examen = p.ExamenOfPanel();
                FrameCreationExamen fen = new FrameCreationExamen(examen);
            }
        });
        pane.add(modifier);
        supprimer = new JButton("Supprimer");
        supprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Examen examen = new Examen();
                PanelExamen p = PanelExamen.panelExamenCliquer;
                p.setBackground(null);
                examen = p.ExamenOfPanel();

                final String networkConfigFile = "network.yaml";
                final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
                final ExamenService examenService = new ExamenService(networkConfig);
                try {
                    examenService.deleteExamen(examen);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                PanelManipulationExamen.afficheExamens().revalidate();
                PanelManipulationExamen.afficheExamens().repaint();
            }
        });
        pane.add(supprimer);

        return pane;
    }
}
