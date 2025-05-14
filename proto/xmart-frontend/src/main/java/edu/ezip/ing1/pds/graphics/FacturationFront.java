package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.business.dto.Paiement;
import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.business.dto.Patient;
import edu.ezip.ing1.pds.business.dto.Patients;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.planning.ExamenService;
import edu.ezip.ing1.pds.services.FactureService;
import edu.ezip.ing1.pds.services.planning.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Comparator;

public class FacturationFront extends JPanel {

    private JTextField montantChamp, dateFactureChamp;
    private JCheckBox regleCheckBox;
    private JComboBox<String> examenCombobox;
    private JComboBox<String> patientCombobox;
    private DefaultTableModel model;
    private JTable table;
    private final FactureService factureService;
    private ArrayList<Examen> examensListe;
    private ArrayList<Patient> patientsListe;

    public FacturationFront() throws InterruptedException, IOException {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final ExamenService examenService = new ExamenService(networkConfig);
        final PatientService patientService = new PatientService(networkConfig);
        this.factureService = new FactureService(networkConfig);

        
        setSize(700, 400);
        
        setLayout(new BorderLayout());

        JPanel panelNord = new JPanel(new GridLayout(5, 2, 5, 5));

        examensListe = new ArrayList<>();
        Examens examens = examenService.selectExamens();
        String[] nomsExamens = new String[0];
        if (examens != null && examens.getExamens() != null) {
        examensListe = new ArrayList<>(examens.getExamens());
        nomsExamens = new String[examensListe.size()];
        for (int i = 0; i < nomsExamens.length; i++) {
        nomsExamens[i] = examensListe.get(i).getNom();
    }
    }
        examenCombobox = new JComboBox<>(nomsExamens);

        patientsListe = new ArrayList<>();
        Patients patients = patientService.selectPatients();
        String[] nomsPatients = new String[0];
        if (patients != null && patients.getPatients() != null) {
        patientsListe = new ArrayList<>(patients.getPatients());
        nomsPatients = new String[patientsListe.size()];
        for (int i = 0; i < nomsPatients.length; i++) {
        nomsPatients[i] = patientsListe.get(i).getNom();
    }
    }
    patientCombobox = new JComboBox<>(nomsPatients);



        montantChamp = new JTextField();
        dateFactureChamp = new JTextField();
        regleCheckBox = new JCheckBox("Facture réglée");
        regleCheckBox.setEnabled(false);

        panelNord.add(new JLabel("Examen :"));
        panelNord.add(examenCombobox);
        panelNord.add(new JLabel("Patient :"));
        panelNord.add(patientCombobox);
        panelNord.add(new JLabel("Montant :"));
        panelNord.add(montantChamp);
        panelNord.add(new JLabel("Date (AAAA-MM-JJ) :"));
        panelNord.add(dateFactureChamp);
        panelNord.add(new JLabel("Réglée :"));
        panelNord.add(regleCheckBox);

        add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID", "Date", "Montant", "Réglée", "Examen", "Patient"};
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
                String montantText = montantChamp.getText();
                String dateText = dateFactureChamp.getText();
                boolean regle = regleCheckBox.isSelected();

                if (montantText.isEmpty() || dateText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double montant = Double.parseDouble(montantText);
                LocalDate date = LocalDate.parse(dateText);

                int selectedIndex = examenCombobox.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un examen", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int patientIndex = patientCombobox.getSelectedIndex();
                if (patientIndex == -1) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un patient", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idExamen = examensListe.get(selectedIndex).getId();
                int idPatient = patientsListe.get(patientIndex).getIdPatient();
                Facture facture = new Facture(date, montant, regle);
                facture.setIdExamen(idExamen);
                facture.setIdPatient(idPatient);
                factureService.insertFacture(facture);

                chargerFactures();
                viderChamps();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Le montant doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Date invalide (format AAAA-MM-JJ)", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                montantChamp.setText(model.getValueAt(i, 2).toString());
                dateFactureChamp.setText(model.getValueAt(i, 1).toString());
                regleCheckBox.setSelected((Boolean) model.getValueAt(i, 3));
                regleCheckBox.setEnabled(true); // revoir si s'active bien lors de selection de ligne

                String nomExamen = model.getValueAt(i, 4).toString();
                for (int j = 0; j < examenCombobox.getItemCount(); j++) {
                    if (examenCombobox.getItemAt(j).equals(nomExamen)) {
                        examenCombobox.setSelectedIndex(j);
                        break;
                    }
                }

                String nomPatient = model.getValueAt(i, 5).toString();
                for (int k = 0; k < patientCombobox.getItemCount(); k++) {
                    if (patientCombobox.getItemAt(k).equals(nomPatient)){
                        break;
                    }
                }
            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    int id = Integer.parseInt(model.getValueAt(i, 0).toString());
                    double montant = Double.parseDouble(montantChamp.getText());
                    LocalDate date = LocalDate.parse(dateFactureChamp.getText());
                    boolean regle = regleCheckBox.isSelected();
                    int idExamen = examensListe.get(examenCombobox.getSelectedIndex()).getId();
                    int idPatient = patientsListe.get(patientCombobox.getSelectedIndex()).getIdPatient();

                    Facture facture = new Facture(date, montant, regle);
                    facture.setIdFacture(id);
                    facture.setIdExamen(idExamen);
                    facture.setIdPatient(idPatient);
                    factureService.updateFacture(facture);

                    chargerFactures();
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
                    Facture facture = new Facture();
                    facture.setIdFacture(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    factureService.deleteFacture(facture);
                    chargerFactures();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        chargerFactures();
    }

    private void chargerFactures() throws IOException, InterruptedException {
        model.setRowCount(0);

        Factures factures = factureService.selectFactures();
        if (factures != null && factures.getFactures()!= null){
            ArrayList<Facture> list = new ArrayList<>(factures.getFactures());
            list.sort(Comparator.comparing(Facture::getDateFacture));
            for (Facture f : list) {
                String nomExamen = "";
                for (Examen e : examensListe) {
                    if (e.getId() == f.getIdExamen()) {
                        nomExamen = e.getNom();
                        break;
                    }
                }
                String nomPatient = "";
                for (Patient p : patientsListe) {
                    if (p.getIdPatient() == f.getIdPatient()) {
                        nomPatient = p.getNom();
                        break;
                    }
                }

                model.addRow(new Object[]{
                        f.getIdFacture(),
                        f.getDateFacture(),
                        f.getMontantFacture(),
                        f.getRegle(),
                        nomExamen,
                        nomPatient
                });
            }
        }

    }

    private void viderChamps() {
        montantChamp.setText("");
        dateFactureChamp.setText("");
        regleCheckBox.setSelected(false);
        regleCheckBox.setEnabled(false);
    }

    
}
