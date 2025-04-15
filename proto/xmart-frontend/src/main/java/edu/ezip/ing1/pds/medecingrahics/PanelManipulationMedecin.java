package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.graphics.Fenetre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PanelManipulationMedecin extends JPanel {

    private static CardLayout cardLayout = new CardLayout();
    private static JPanel cardPanel = new JPanel(cardLayout);
    private JLabel boutonMedecin;
    private JLabel boutonPlanning;
    private JLabel boutonDisponibilite;


    public PanelManipulationMedecin() throws IOException, InterruptedException {
        JPanel panelNord = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNord.setPreferredSize(new Dimension(50, 50));

        panelNord.add(panelBoutonMedecin());
        panelNord.add(panelBoutonPlanning());
        panelNord.add(panelBoutonDisponibilite());

        cardPanel.add(new PanelAfficheMedecin(), "AfficheMedecin");


        setLayout(new BorderLayout());
        add(panelNord, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
    }


    public JPanel panelBoutonMedecin(){
        JPanel panel = new JPanel();
        boutonMedecin = Fenetre.createLabel("Médecin");
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(boutonMedecin);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "AfficheMedecin");
            }
        });
        return panel;
    }
    public JPanel panelBoutonPlanning(){
        JPanel panel = new JPanel();
        boutonPlanning = Fenetre.createLabel("Planning");
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(boutonPlanning);
        return panel;
    }
    public JPanel panelBoutonDisponibilite(){
        JPanel panel = new JPanel();
        boutonDisponibilite = Fenetre.createLabel("Disponibilité");
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(boutonDisponibilite);
        return panel;
    }




}
