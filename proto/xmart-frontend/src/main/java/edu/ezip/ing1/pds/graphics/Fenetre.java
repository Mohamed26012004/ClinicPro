package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.medecingrahics.InsertUpdateMedecinWindows;
import edu.ezip.ing1.pds.medecingrahics.PanelManipulationExamen;
import edu.ezip.ing1.pds.medecingrahics.PanelManipulationSalle;

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

    public Fenetre() throws IOException, InterruptedException {
            super("ClinicPro");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            contentPane = (JPanel)getContentPane();
            contentPane.setLayout(new BorderLayout());
//            contentPane.add(menuBar(), BorderLayout.NORTH);
            contentPane.add(PanelGauche(), BorderLayout.WEST);
            contentPane.add(new PanelManipulationExamen(), BorderLayout.CENTER);


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
            pane.add(panelBoutonMedecint());
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
            panelExamen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            panelExamen.add(boutonExamem);
            effetSurBouton(panelExamen);
            panelExamen.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    contentPane.removeAll();
                    try {
                        contentPane.add(new PanelManipulationExamen(), BorderLayout.CENTER);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
//                    contentPane.add(menuBar(), BorderLayout.NORTH);
                    contentPane.add(PanelGauche(), BorderLayout.WEST);
                    contentPane.repaint();
                    contentPane.revalidate();
                }
            });
            return panelExamen;
        }

    public JPanel panelBoutonSalle(){
        boutonSalle = createLabel("Salle");
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(boutonSalle);
        effetSurBouton(panel);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPane.removeAll();
                try {
                    contentPane.add(new PanelManipulationSalle(),BorderLayout.CENTER);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                contentPane.add(PanelGauche(), BorderLayout.WEST);
//                contentPane.add(menuBar(), BorderLayout.NORTH);
                contentPane.repaint();
                contentPane.revalidate();
            }
        });
        return panel;
    }

    public JPanel panelBoutonEquipement(){
        boutonEquipement = createLabel("Equipement");
        JPanel panelExamen = new JPanel();
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


    public JPanel panelBoutonMedecint(){
        JLabel label = createLabel("Médecin");
        JPanel panelExamen = new JPanel();
        panelExamen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelExamen.add(label);
        effetSurBouton(panelExamen);
        panelExamen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    InsertUpdateMedecinWindows f = new InsertUpdateMedecinWindows(null);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return panelExamen;
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
