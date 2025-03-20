package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ExamenService;
import edu.ezip.ing1.pds.servicesplanning.SalleService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FrameCreationExamen extends JFrame {

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final ExamenService examenService = new ExamenService(networkConfig);

    private JLabel labelNom;
    private JLabel labelCout;
    private JLabel labelDuree;
    private JTextField valueNom;
    private JTextField valueCout;
    private JTextField valueDuree;
    private JButton enregistrer;
    private JButton annuler;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public FrameCreationExamen(Examen examen){
        super("Création Examen");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(formulaire(examen));
        add(boutons(examen), BorderLayout.SOUTH);

        setVisible(true);
    }

    public JPanel formulaire(Examen exam) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 5, 5));

        labelNom = new JLabel("Nom : ");
        labelCout = new JLabel("Coût : ");
        labelDuree = new JLabel("Duréé (HH:mm)");

        if (exam != null){
            valueNom = new JTextField(exam.getNom());
            valueCout = new JTextField(String.valueOf(exam.getCout()));;
            valueDuree = new JTextField(exam.getDuree().format(formatter));
        }else {
            valueNom = new JTextField("");
            valueCout = new JTextField("");;
            valueDuree = new JTextField("");
        }

        panel.add(labelNom);
        panel.add(valueNom);
        panel.add(labelCout);
        panel.add(valueCout);
        panel.add(labelDuree);
        panel.add(valueDuree);

        return panel;
    }


    public JPanel boutons(Examen exam){
        JPanel panel = new JPanel(new FlowLayout());
        enregistrer = new JButton("Enregistrer");
        annuler = new JButton("Annuler");

        panel.add(enregistrer);
        panel.add(annuler);

        enregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = valueNom.getText();
                Double cout;
                LocalTime duree;

                try {
                    cout = Double.parseDouble(valueCout.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Saisissez des nombres", "Erreur sur coût", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                try {
                    duree = LocalTime.parse(valueDuree.getText(), formatter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Respectez les formats de la durée", "Erreur sur durée", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                if(cout<0){
                    JOptionPane.showMessageDialog(null, "Saisissez des nombres positifs", "Erreur sur coût", JOptionPane.ERROR_MESSAGE);
                }else{
                    Examen examen = new Examen(nom, cout, duree);

                    if (exam != null){
                        try {
                            examenService.deleteExamen(exam);
                            examenService.insertExamen(examen);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }else{
                        try {
                            examenService.insertExamen(examen);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }

                FrameCreationExamen.this.dispose();

            }
        });

        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameCreationExamen.this.dispose();
            }
        });

        return panel;
    }
}
