package edu.ezip.ing1.pds.graphics;

import java.awt.*;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;


public class FenetreMedecin extends JFrame{


    private JPanel contentPane;
    private static JLabel boutonAntecedentMedical;
    private static JLabel boutonCompteRendu;
    private static JLabel boutonDiagnostic;
    private static JLabel boutonTraitement;

    public FenetreMedecin() throws IOException, InterruptedException {
        super("Médecins - ClinicPro");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

//        UIManager.put("Label.font", new Font("Impact", Font.PLAIN, 14));
//        UIManager.put("Button.font", new Font("Impact", Font.BOLD, 14));

        contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());
//            contentPane.add(menuBar(), BorderLayout.NORTH);
        contentPane.add(PanelGauche(), BorderLayout.WEST);
        contentPane.add(new JLabel("Sélectionnez un sous-dossier à traiter"), BorderLayout.CENTER);


    }

    public JPanel PanelGauche(){
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        pane.add(panelBoutonAntecedentMedical());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(panelBoutonCompteRendu());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(panelBoutonDiagnostic());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));
        pane.add(panelBoutonTraitement());
        pane.add(Box.createRigidArea(new Dimension(0, 15)));


        return pane;
    }

    public JPanel panelBoutonAntecedentMedical(){
        boutonAntecedentMedical = createLabel("Antecedent Medical");
        JPanel panelAntecedentMedical = new JPanel();
        panelAntecedentMedical.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelAntecedentMedical.add(boutonAntecedentMedical);
        effetSurBouton(panelAntecedentMedical);
        panelAntecedentMedical.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPane.removeAll();
                try {
                    contentPane.add(new AntecedentMedicalFront(), BorderLayout.CENTER);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                contentPane.add(PanelGauche(), BorderLayout.WEST);
                contentPane.repaint();
                contentPane.revalidate();
            }
        });
        return panelAntecedentMedical;
    }

    public JPanel panelBoutonCompteRendu(){
        boutonCompteRendu = createLabel("Compte Rendu");
        JPanel panelCompteRendu = new JPanel();
        panelCompteRendu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelCompteRendu.add(boutonCompteRendu);
        effetSurBouton(panelCompteRendu);
        panelCompteRendu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPane.removeAll();
                try {
                    contentPane.add(new CompteRenduFront(), BorderLayout.CENTER);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                contentPane.add(PanelGauche(), BorderLayout.WEST);
                contentPane.repaint();
                contentPane.revalidate();
            }
        });
        return panelCompteRendu;
    }


    public JPanel panelBoutonDiagnostic(){
        boutonDiagnostic = createLabel("Diagnostic");
        JPanel panelDiagnostic = new JPanel();
        panelDiagnostic.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelDiagnostic.add(boutonDiagnostic);
        effetSurBouton(panelDiagnostic);
        panelDiagnostic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPane.removeAll();
                try {
                    contentPane.add(new DiagnosticFront(), BorderLayout.CENTER);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                contentPane.add(PanelGauche(), BorderLayout.WEST);
                contentPane.repaint();
                contentPane.revalidate();
            }
        });
        return panelDiagnostic;
    }

    public JPanel panelBoutonTraitement(){
        boutonTraitement = createLabel("Traitement");
        JPanel panelTraitement = new JPanel();
        panelTraitement.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelTraitement.add(boutonTraitement);
        effetSurBouton(panelTraitement);
        panelTraitement.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPane.removeAll();
                try {
                    contentPane.add(new TraitementFront(), BorderLayout.CENTER);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                contentPane.add(PanelGauche(), BorderLayout.WEST);
                contentPane.repaint();
                contentPane.revalidate();
            }
        });
        return panelTraitement;
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
