package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.graphics.examen.PanelManipulationExamen;
import edu.ezip.ing1.pds.graphics.medecin.PanelManipulationMedecin;
import edu.ezip.ing1.pds.graphics.salle.PanelManipulationSalle;

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

        pane.add(panelBoutonDiagnostic());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));

        return pane;
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
        panelExamen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EquipementFront f = new EquipementFront();
            }
        });
        return panelExamen;
    }

    public JPanel panelBoutonPaiement(){
        boutonExamem = createLabel("Paiement");
        JPanel panelExamen = new JPanel();
        panelExamen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelExamen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelExamen.add(boutonExamem);
        panelExamen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PaiementFront f = new PaiementFront();
            }
        });
        return panelExamen;
    }

    public JPanel panelBoutonDiagnostic(){
        boutonExamem = createLabel("Diagnostic");
        JPanel panelExamen = new JPanel();
        panelExamen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelExamen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelExamen.add(boutonExamem);
        panelExamen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DiagnosticFront d = new DiagnosticFront();
            }
        });
        return panelExamen;
    }


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

//    public JMenuBar menuBar(){
//        JMenuBar bar = new JMenuBar();
//        bar.setPreferredSize(new Dimension(0, 35));
//        JMenu compte = new JMenu("Compte");
//        compte.setFont(new Font("Arial", Font.PLAIN, 16));
//        bar.add(compte);
//
//        return bar;
//    }

}
