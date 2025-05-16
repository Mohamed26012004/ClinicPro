package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.AntecedentMedical;
import edu.ezip.ing1.pds.business.dto.AntecedentMedicals;
import edu.ezip.ing1.pds.business.dto.Patient;
import edu.ezip.ing1.pds.business.dto.Patients;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.planning.PatientService;
import edu.ezip.ing1.pds.servicesdpi.AntecedentMedicalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class AntecedentMedicalFront extends JPanel {

    private JTextField typeChamp, descriptionChamp;
    private JComboBox<Patient> patientComboBox;
    private DefaultTableModel model;
    private JTable table;
    private final AntecedentMedicalService antecedentMedicalService;
    private ArrayList<Patient> patientsListe;

    public AntecedentMedicalFront() throws IOException, InterruptedException {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final PatientService patientService = new PatientService(networkConfig);
        this.antecedentMedicalService = new AntecedentMedicalService(networkConfig);

        setSize(700, 400);


        setLayout(new BorderLayout());

        JPanel panelNord = new JPanel(new GridLayout(3, 2, 5, 5));

        patientComboBox = new JComboBox<>();
        patientsListe = new ArrayList<>();
        Patients patients = patientService.selectPatients();

        if (patients != null && patients.getPatients() != null) {
            patientsListe = new ArrayList<>(patients.getPatients());
            for (Patient p : patientsListe) {
                patientComboBox.addItem(p);
            }
        }

        typeChamp = new JTextField();
        descriptionChamp = new JTextField();

        panelNord.add(new JLabel("Antécédent médical :"));
        panelNord.add(typeChamp);
        panelNord.add(new JLabel("Description :"));
        panelNord.add(descriptionChamp);
        panelNord.add(new JLabel("Patient :"));
        panelNord.add(patientComboBox);

        add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID", "ID Patient", "Antécédent médical", "Description",};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelSud = new JPanel();
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");

        panelSud.add(boutonAjouter);
        panelSud.add(boutonModifier);
        panelSud.add(boutonSupprimer);

        add(panelSud, BorderLayout.SOUTH);

        boutonAjouter.addActionListener(e -> {
            try {
                String type = typeChamp.getText().trim();
                String description = descriptionChamp.getText().trim();
                Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();

                if (type.isEmpty() || description.isEmpty() || selectedPatient == null) {
                    JOptionPane.showMessageDialog(null, "Veuillez s'il vous plaît remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AntecedentMedical antecedent = new AntecedentMedical();
                antecedent.setType_antecedentMedical(type);
                antecedent.setDescription_antecedentMedical(description);
                antecedent.setIdPatient(selectedPatient.getIdPatient());

                antecedentMedicalService.insertAntecedentMedical(antecedent);
                chargerAntecedents();
                viderChamps();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                typeChamp.setText(model.getValueAt(i, 2).toString());
                descriptionChamp.setText(model.getValueAt(i, 3).toString());
                int idPatient = Integer.parseInt(model.getValueAt(i, 1).toString());

                for (int j = 0; j < patientComboBox.getItemCount(); j++) {
                    if (patientComboBox.getItemAt(j).getIdPatient() == idPatient) {
                        patientComboBox.setSelectedIndex(j);
                        break;
                    }
                }
            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    AntecedentMedical antecedent = new AntecedentMedical();
                    antecedent.setId_antecedentMedical(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    antecedent.setType_antecedentMedical(typeChamp.getText().trim());
                    antecedent.setDescription_antecedentMedical(descriptionChamp.getText().trim());

                    Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
                    if (selectedPatient != null) {
                        antecedent.setIdPatient(selectedPatient.getIdPatient());
                    }

                    antecedentMedicalService.updateAntecedentMedical(antecedent);
                    chargerAntecedents();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonSupprimer.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    AntecedentMedical antecedent = new AntecedentMedical();
                    antecedent.setId_antecedentMedical(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    antecedentMedicalService.deleteAntecedentMedical(antecedent);
                    chargerAntecedents();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        chargerAntecedents();
    }

    private void chargerAntecedents() throws IOException, InterruptedException {
        model.setRowCount(0);
        AntecedentMedicals antecedents = antecedentMedicalService.selectantecedentMedicals();
        if (antecedents != null && antecedents.getAntecedentMedicals() != null) {
            for (AntecedentMedical a : antecedents.getAntecedentMedicals()) {
                model.addRow(new Object[]{
                        a.getId_antecedentMedical(),
                        a.getIdPatient(),
                        a.getType_antecedentMedical(),
                        a.getDescription_antecedentMedical(),

                });
            }
        }
    }

    private void viderChamps() {
        typeChamp.setText("");
        descriptionChamp.setText("");

    }
}
