package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ExamenService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FenetreExamen extends JFrame {

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final ExamenService examenService = new ExamenService(networkConfig);

    private JLabel nom;
    private JLabel cout;
    private JLabel numSalle;
    private JPanel contentPane;
    private JTextField valeurNom;
    private JTextField valeurCout;
    private JTextField valeurNumeroSalle;

    protected static JButton enregistrer;
    protected static JButton annuler;

    public FenetreExamen(Examen examen){
        super("Examen");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = (JPanel)getContentPane();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 5, 5));
        nom = new JLabel("Nom :");
        cout = new JLabel("Coût :");
        numSalle = new JLabel("Numéro de salle :");


        if(examen == null){
            valeurNom = new JTextField("");
            valeurCout = new JTextField("");
            valeurNumeroSalle = new JTextField("");
            contentPane.add(boutons("insert"), BorderLayout.SOUTH);
        }else{
            valeurNom  = new JTextField(examen.getNom());
            valeurCout = new JTextField(String.valueOf(examen.getCout()));
            valeurNumeroSalle = new JTextField(examen.getNumeroSalle());
            contentPane.add(boutons("update"), BorderLayout.SOUTH);
        }
        panel.add(nom);
        panel.add(valeurNom);
        panel.add(cout);
        panel.add(valeurCout);
        panel.add(numSalle);
        panel.add(valeurNumeroSalle);

        contentPane.add(panel);

        setVisible(true);
    }

    public JPanel boutons(String action){
        JPanel pane = new JPanel(new FlowLayout());

        enregistrer = new JButton("Enregister");
        if(action.equals("insert")){
            enregistrer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nom = valeurNom.getText();
                    double cout = Double.parseDouble(valeurCout.getText());
                    String numSlle = valeurNumeroSalle.getText();
                    Examen examen = new Examen(nom, cout, numSlle);
                    PanelMaxime.afficheExamens().removeAll();
                    try {
                        examenService.insertExamen(examen);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    PanelMaxime.afficheExamens().revalidate();
                    PanelMaxime.afficheExamens().repaint();
                    FenetreExamen.this.dispose();
                }
            });
        }else if(action.equals("update")){
            enregistrer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    PanelExamen pane = PanelExamen.examenSelected;
                    Examen oldExamen = pane.examenDuPanel();

                    String nom = valeurNom.getText();
                    double cout = Double.parseDouble(valeurCout.getText());
                    String numSlle = valeurNumeroSalle.getText();
                    Examen newExamen = new Examen(nom, cout, numSlle);

                    PanelMaxime.afficheExamens().removeAll();
                    try {

                        newExamen.setId((examenService.selectOneExamen(oldExamen).getId()));
                        examenService.updateExamen(newExamen);

                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    PanelMaxime.afficheExamens().revalidate();
                    PanelMaxime.afficheExamens().repaint();
                    FenetreExamen.this.dispose();
                }
            });
        }
        pane.add(enregistrer);

        annuler = new JButton("Annuler");
        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                contentPane.revalidate();
                contentPane.repaint();
                FenetreExamen.this.dispose();

            }
        });
        pane.add(annuler);

        return pane;
    }
}
