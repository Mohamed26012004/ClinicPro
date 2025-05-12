package edu.ezip.ing1.pds.graphics.examen;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.services.planning.ExamenService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FrameCreationExamen extends JFrame {

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final ExamenService examenService = new ExamenService(networkConfig);

    private JLabel labelId;
    private JLabel labelNom;
    private JLabel labelCout;
    private JLabel labelDuree;
    private JLabel valueId;
    private JTextField valueNom;
    private JTextField valueCout;
    private JComboBox valueDuree;
    private JButton enregistrer;
    private JButton annuler;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private String  [] tabDuree = {"00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00"};
    private final String msgErreurChampVide = "Veillez remplir tous les champs avant d'enregistrer l'examen.";

    public FrameCreationExamen(Examen examen){
        super("Création Examen");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(formulaire(examen));
        add(boutons(examen), BorderLayout.SOUTH);

        setVisible(true);
    }

    public JPanel formulaire(Examen exam) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        labelId = Fenetre.createLabel("Identifiant : ");
        labelNom = Fenetre.createLabel("Nom : ");
        labelCout = Fenetre.createLabel("Coût (€) : ");
        labelDuree = Fenetre.createLabel("Durée : ");
        valueDuree = new JComboBox<>(tabDuree);

        if (exam != null){
            valueId = new JLabel(String.valueOf(exam.getId()));
            valueNom = new JTextField(exam.getNom());
            valueCout = new JTextField(String.valueOf(exam.getCout()));;

        }else {
            valueId = Fenetre.createLabel("######");
            valueNom = Fenetre.createTextField("");
            valueCout = Fenetre.createTextField("");

        }

        panel.add(labelId);
        panel.add(valueId);
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

        enregistrer.setBackground(new Color(72, 255, 0));
        annuler.setBackground(new Color(255, 65, 65));

        panel.add(enregistrer);
        panel.add(annuler);

        enregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nom = valueNom.getText();
                double cout;
                LocalTime duree = LocalTime.parse((String) Objects.requireNonNull(valueDuree.getSelectedItem()), formatter);

                if( valueNom.getText() == null || valueCout.getText() == null || Objects.equals(nom, "")){
                    JOptionPane.showMessageDialog(
                            null, msgErreurChampVide, "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }else {

                    try {
                        cout = Double.parseDouble(valueCout.getText());

                        if(cout<0){
                            JOptionPane.showMessageDialog(null, "Le Coût doit être un nombre positif.", "Erreur sur coût", JOptionPane.ERROR_MESSAGE);
                        }else{
                            Examen examen = new Examen(nom, cout, duree);
                            if (exam != null){
                                try {
                                    exam.setNom(nom);
                                    exam.setDuree(duree);
                                    exam.setCout(cout);
                                    exam.setId(Integer.parseInt(valueId.getText()));
                                    examenService.updateExamen(exam);
                                    FrameCreationExamen.this.dispose();
                                    PanelManipulationExamen.chargerExamens();
                                    JOptionPane.showMessageDialog(null, "Mise à jour effectuée.", "Message", JOptionPane.INFORMATION_MESSAGE);
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }else{
                                try {
                                    examenService.insertExamen(examen);
                                    FrameCreationExamen.this.dispose();
                                    PanelManipulationExamen.chargerExamens();
                                    JOptionPane.showMessageDialog(null, "L'examen a bien été ajouté.", "Message", JOptionPane.INFORMATION_MESSAGE);
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Le Coût doit être un nombre positif.", "Erreur sur coût", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }

                }

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