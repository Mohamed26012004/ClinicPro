package edu.ezip.ing1.pds.graphics.medecin;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Patient;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.graphics.examen.FrameCreationExamen;
import edu.ezip.ing1.pds.graphics.examen.PanelManipulationExamen;
import edu.ezip.ing1.pds.services.planning.ExamenService;
import edu.ezip.ing1.pds.services.planning.PatientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FrameInsertPatient extends JFrame {

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final PatientService patientService = new PatientService(networkConfig);

    private JLabel labelPrenom;
    private JLabel labelNom;
    private JLabel labelTelephone;
    private JLabel labelAdresse;
    private JTextField valuePrenom;
    private JTextField valueNom;
    private JTextField valueTelephone;
    private JTextField valueAdresse;
    private JButton enregistrer;
    private JButton annuler;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        private final String msgErreurChampVide = "Veillez remplir tous les champs avant d'enregistrer le patient.";

    public FrameInsertPatient(){
        super("Insérer Patient");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(formulaire());
        add(boutons(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public JPanel formulaire() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        labelPrenom = Fenetre.createLabel("Prénom : ");
        labelNom = Fenetre.createLabel("Nom : ");
        labelTelephone = Fenetre.createLabel("Téléphone : ");
        labelAdresse = Fenetre.createLabel("Adresse : ");

        valuePrenom = Fenetre.createTextField("");
        valueNom = Fenetre.createTextField("");
        valueTelephone = Fenetre.createTextField("");
        valueAdresse = Fenetre.createTextField("");

        panel.add(labelNom);
        panel.add(valueNom);
        panel.add(labelPrenom);
        panel.add(valuePrenom);
        panel.add(labelTelephone);
        panel.add(valueTelephone);
        panel.add(labelAdresse);
        panel.add(valueAdresse);

        return panel;
    }


    public JPanel boutons(){
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

                if(valueNom.getText() == null || valueTelephone.getText() == null || valuePrenom.getText() == null || valueAdresse == null){
                    JOptionPane.showMessageDialog(
                            null, msgErreurChampVide, "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }else{
                    Patient p = new Patient();
                    p.setNom(valueNom.getText());
                    p.setPrenom(valuePrenom.getText());
                    p.setTelephone(valueTelephone.getText());
                    p.setAdresse(valueAdresse.getText());
                    try {
                        patientService.insertPatient(p);
                        FrameDeSelectionPatient.chargerPatients();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    FrameInsertPatient.this.dispose();
                }

            }
        });

        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameInsertPatient.this.dispose();
            }
        });

        return panel;
    }
}
