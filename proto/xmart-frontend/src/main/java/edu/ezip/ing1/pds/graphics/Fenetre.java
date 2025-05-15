package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.graphics.examen.PanelManipulationExamen;
import edu.ezip.ing1.pds.graphics.medecin.PanelManipulationMedecin;
import edu.ezip.ing1.pds.graphics.salle.PanelManipulationSalle;
import org.stringtemplate.v4.ST;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class Fenetre extends JFrame{

    private JPanel contentPane;
    private static JLabel boutonExamem;
    private static JLabel boutonSalle;
    private static JLabel boutonEquipement;
    private static JLabel boutonPaiement;
    private static JLabel boutonMaintenance;
    private static JLabel boutonStatistiques;
    private static JLabel boutonFacture;


    private static CardLayout card = new CardLayout();
    private static JPanel cartePanel = new JPanel(card);



    public Fenetre() throws IOException, InterruptedException {
        super("ClinicPro");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());
        //           contentPane.add(menuBar(), BorderLayout.NORTH);
        contentPane.add(PanelGauche(), BorderLayout.WEST);

        cartePanel.add(new PanelManipulationExamen(), "Examen");
        cartePanel.add(new PanelManipulationSalle(), "Salle");
        cartePanel.add(new PanelManipulationMedecin(), "Medecin");
        cartePanel.add(new FacturationFront(), "Facture");
        cartePanel.add(new PaiementFront(), "Paiement");

        cartePanel.add(new EquipementFront(), "Equipement");
        cartePanel.add(new MaintenanceFront(), "Maintenance");



        // Panneau Statistiques  (6 cases)
        JPanel panelStatistiques = new JPanel(new GridLayout(4, 3));
        panelStatistiques.add(new TotalPaiementFront());
        panelStatistiques.add(new CoutGlobalParJourFront());
        panelStatistiques.add(new CoutGlobalParMoisFront());
        panelStatistiques.add(new TotalMaintenanceFront());
        panelStatistiques.add(new TotalCoutFront());
        panelStatistiques.add(new RevenuesNetParJour());
        panelStatistiques.add(new RevenuesNetParMois());
        for (int i = 0; i < 4; i++) {
            panelStatistiques.add(new JPanel());
        }
        cartePanel.add(panelStatistiques, "Statistiques");

        contentPane.add(cartePanel, BorderLayout.CENTER);
    }

    public JPanel PanelGauche(){
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        pane.add(panelBoutonExamen());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(panelBoutonSalle());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(panelBoutonEquipement());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(panelBoutonMedecin());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(panelBoutonPaiement());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(panelBoutonStatistiques());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));

        pane.add(panelBoutonDiagnostic());
        pane.add(panelBoutonFacture());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(panelBoutonMaintenance());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(Box.createRigidArea(new Dimension(0, 15)));

//        pane.add(panelBoutonDiagnostic());
//        pane.add(Box.createRigidArea(new Dimension(0, 15)));

        return pane;
    }

    public JPanel panelBoutonStatistiques(){
        boutonStatistiques = createLabel("Statistiques");
        JPanel panelStatistiques = new JPanel();
        panelStatistiques.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelStatistiques.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelStatistiques.add(boutonStatistiques);
        effetSurBouton(panelStatistiques);
        panelStatistiques.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                card.show(cartePanel, "Statistiques");
            }
        });
        return panelStatistiques;
    }

    public JPanel panelBoutonExamen(){
        boutonExamem = createLabel("Examen");
        JPanel panelExamen = new JPanel();
        panelExamen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelExamen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelExamen.add(boutonExamem);
        effetSurBouton(panelExamen);
        panelExamen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                card.show(cartePanel, "Examen");
            }
        });
        return panelExamen;
    }

    public JPanel panelBoutonSalle(){
        boutonSalle = createLabel("Salle");
        JPanel panel = new JPanel();
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(boutonSalle);
        effetSurBouton(panel);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                card.show(cartePanel, "Salle");
            }
        });
        return panel;
    }

    public JPanel panelBoutonEquipement(){
        boutonEquipement = createLabel("Equipement");
        JPanel panelExamen = new JPanel();
        panelExamen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelExamen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelExamen.add(boutonEquipement);
        effetSurBouton(panelExamen);
        panelExamen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                card.show(cartePanel, "Equipement");
            }
        });
        return panelExamen;
    }

    public JPanel panelBoutonPaiement(){
        JLabel label = createLabel("Paiement");
        JPanel panel = new JPanel();
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(label);
        effetSurBouton(panel);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               card.show(cartePanel, "Paiement");
                DiagnosticFront d = new DiagnosticFront();

            }
        });
        return panelExamen;
    }

    public JPanel panelBoutonMaintenance(){
        boutonMaintenance = createLabel("Maintenance");
        JPanel panelExamen = new JPanel();
        panelExamen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelExamen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelExamen.add(boutonMaintenance);
        effetSurBouton(panelExamen);
        panelExamen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                card.show(cartePanel, "Maintenance");
            }
        });
        return panel;
    }

//    public JPanel panelBoutonDiagnostic(){
//        boutonExamem = createLabel("Diagnostic");
//        JPanel panelExamen = new JPanel();
//        panelExamen.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        panelExamen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
//        panelExamen.add(boutonExamem);
//        panelExamen.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                DiagnosticFront d = new DiagnosticFront();
//            }
//        });
//        return panelExamen;
//    }


    public JPanel panelBoutonMedecin(){
        JLabel label = createLabel("Médecin");
        JPanel panel = new JPanel();
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(label);
        effetSurBouton(panel);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               card.show(cartePanel, "Medecin");
            }
        });
        return panel;
    }

     public JPanel panelBoutonFacture(){
        JLabel label = createLabel("Facture");
        JPanel panel = new JPanel();
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(label);
        effetSurBouton(panel);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               card.show(cartePanel, "Facture");
            }
        });
        return panel;
    }

    public static void effetSurBouton(JPanel p){
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                p.setBackground(new Color(83, 83, 83));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                p.setBackground(new Color(123,123,123));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                p.setBackground(null);
            }
        });
    }

    public static JLabel createLabel(String text){
        JLabel label = new JLabel(java.lang.String.valueOf(text));
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    public static JTextField createTextField(String text){
        JTextField t = new JTextField(text);
        t.setFont(new Font("Arial", Font.PLAIN, 14));
        return t;
    }


}
