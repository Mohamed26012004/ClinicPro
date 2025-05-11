package edu.ezip.ing1.pds.graphics.medecin;

import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.graphics.Fenetre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PanelManipulationMedecin extends JPanel {

    public static CardLayout cardLayout = new CardLayout();
    public static JPanel cardPanel = new JPanel(cardLayout);
    private JLabel boutonMedecin;
    private JLabel boutonPlanning;
    private JLabel boutonDisponibilite;
    private static Medecin medecinToUpdate;


    public PanelManipulationMedecin() throws IOException, InterruptedException {
        JPanel panelNord = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNord.setPreferredSize(new Dimension(50, 50));

        panelNord.add(panelBoutonMedecin());
        panelNord.add(panelBoutonPlanning());
        panelNord.add(panelBoutonDisponibilite());

//        Component tabPanel [] = panelNord.getComponents();
//        for (int i = 0; i<tabPanel.length; i++){
//            int finalI = i;
//            tabPanel[i].addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    JPanel p = (JPanel) e.getSource();
//                    if(tabPanel[finalI] == p){
//                        p.setBackground(new Color(123, 123, 213));
//                    }else{
//                        p.setBackground(null);
//                    }
//                }
//
//                @Override
//                public void mouseExited(MouseEvent e) {
//                    JPanel p = (JPanel) e.getSource();
//                    if(tabPanel[finalI] != p){
//                        p.setBackground(null);
//                    }
//
//                }
//
//                @Override
//                public void mouseEntered(MouseEvent e) {
//                    JPanel p = (JPanel) e.getSource();
//                    if(tabPanel[finalI] == p){
//                        p.setBackground(new Color(149, 149, 207));
//                    }
//                }
//            });
//        }

        cardPanel.add(new PanelAfficheMedecin(), "AfficheMedecin");


        setLayout(new BorderLayout());
        add(panelNord, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
    }


    public JPanel panelBoutonMedecin(){
        JPanel panel = new JPanel();
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonPlanning = Fenetre.createLabel("Planning");
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(boutonPlanning);
        return panel;
    }
    public JPanel panelBoutonDisponibilite(){
        JPanel panel = new JPanel();
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonDisponibilite = Fenetre.createLabel("Disponibilité");
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(boutonDisponibilite);
        return panel;
    }

    public static Medecin getMedecinToUpdate() {
        return medecinToUpdate;
    }

    public static void setMedecinToUpdate(Medecin medecinToUpdate) {
        PanelManipulationMedecin.medecinToUpdate = medecinToUpdate;
    }



}
