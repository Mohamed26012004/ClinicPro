package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.business.dto.Patient;
import edu.ezip.ing1.pds.business.dto.Patients;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.FactureService;
import edu.ezip.ing1.pds.services.planning.ExamenService;
import edu.ezip.ing1.pds.services.planning.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class FacturationFront extends JPanel {

    // Déclaration des composants de l'ihm
    private JTextField montantField, dateFactureField;
    private JCheckBox regleCheckBox;
    private JComboBox<String> examenComboBox;
    private JComboBox<String> patientComboBox;
    private DefaultTableModel tableModel;
    private JTable facturesTable;

    // différents services pour communiquer avec le back
    private final FactureService factureService;
    private final ExamenService examenService;
    private final PatientService patientService;


    // Listes pour stocker les données récupérées
    private ArrayList<Examen> listeExamens;
    private ArrayList<Patient> listePatients;


    public FacturationFront() throws IOException, InterruptedException {
        // Configuration du réseau
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);

        // Initialisation des services
        this.factureService = new FactureService(networkConfig);
        this.examenService = new ExamenService(networkConfig);
        this.patientService = new PatientService(networkConfig);


        // Configuration du panel
        setLayout(new BorderLayout());
        setSize(800, 600);

        // Initialisation des listes
        listeExamens = new ArrayList<>();
        listePatients = new ArrayList<>();

        // Création du panneau de formulaire pour ajouter/modifier les factures
        JPanel panelNord = new JPanel(new GridLayout(6, 2, 10, 10));

        // initialisation des composants du formulaire
        examenComboBox = new JComboBox<>();
        patientComboBox = new JComboBox<>();
        montantField = new JTextField();
        dateFactureField = new JTextField();
        regleCheckBox = new JCheckBox("Facture réglée");
        regleCheckBox.setEnabled(false); // elle est désactivée par défaut voir pigeot cours

        // Ajout des composants au panneau de formulaire
        panelNord.add(new JLabel("Examen :"));
        panelNord.add(examenComboBox);
        panelNord.add(new JLabel("Patient :"));
        panelNord.add(patientComboBox);
        panelNord.add(new JLabel("Montant :"));
        panelNord.add(montantField);
        panelNord.add(new JLabel("Date (AAAA-MM-JJ) :"));
        panelNord.add(dateFactureField);
        panelNord.add(new JLabel("État :"));
        panelNord.add(regleCheckBox);

        // ajout du panneau de formulaire en haut de la fenêtre
        add(panelNord, BorderLayout.NORTH);

        // Configuration du tableau pour afficher les factures
        String[] columnNames = {"ID", "Date", "Montant", "Réglée", "Examen", "Patient"};
        tableModel = new DefaultTableModel(columnNames, 0);
        facturesTable = new JTable(tableModel);
        add(new JScrollPane(facturesTable), BorderLayout.CENTER);

        // Création du panneau pour les boutons
        JPanel boutonsPanel = new JPanel();
        JButton ajouterButton = new JButton("Ajouter");
        JButton modifierButton = new JButton("Modifier");
        JButton supprimerButton = new JButton("Supprimer");

        boutonsPanel.add(ajouterButton);
        boutonsPanel.add(modifierButton);
        boutonsPanel.add(supprimerButton);

        // Ajout du panneau de boutons en bas de la fenêtre
        add(boutonsPanel, BorderLayout.SOUTH);

        // Chargement initial des données
        chargerDonneesInitiales();

        // Action des différents boutons
        ajouterButton.addActionListener(e -> ajouterFacture());
        modifierButton.addActionListener(e -> modifierFacture());
        supprimerButton.addActionListener(e -> supprimerFacture());


        // champ remplit lorsqu'une ligne est sélectionnée et que la selection est closee
        facturesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && facturesTable.getSelectedRow() != -1) {
                remplirChampsDepuisTableau();
            }
        });

        // Mise à jour des listes d'examens et de patients en cas d'ajout
        Timer timer = new Timer(30000, e -> {
            try {
                chargerExamens();
                chargerPatients();
            } catch (Exception ex) {
                System.err.println("Erreur de mise à jour : " + ex.getMessage());
            }});

        timer.start();
    }

    // chargement des données
    private void chargerDonneesInitiales() {
        try {
            chargerExamens();
            chargerPatients();
            chargerToutesLesFactures();
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement initial des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Ajouter une facture

    private void ajouterFacture() {
        try {
            // Validation des champs
            if (montantField.getText().isEmpty() && dateFactureField.getText().isEmpty() &&
                    examenComboBox.getSelectedIndex() == -1 && patientComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // récupération des données du formulaire et conversion dans le bon format
            double montant = Double.parseDouble(montantField.getText());
            LocalDate date = LocalDate.parse(dateFactureField.getText());
            int idExamen = listeExamens.get(examenComboBox.getSelectedIndex()).getId();
            int idPatient = listePatients.get(patientComboBox.getSelectedIndex()).getIdPatient();

            // Création de l'objet Facture
            Facture facture = new Facture(date, montant, regleCheckBox.isSelected()); //boolean methonde
            facture.setIdExamen(idExamen);
            facture.setIdPatient(idPatient);

            // Insertion de la facture via le service
            factureService.insertFacture(facture);

            // refresh
            chargerToutesLesFactures();
            viderChamps();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le montant doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez AAAA-MM-JJ.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Modifier la facture sélectionnée
    private void modifierFacture() {
        int selectedRow = facturesTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                // Récupération des données
                int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                double montant = Double.parseDouble(montantField.getText());
                LocalDate date = LocalDate.parse(dateFactureField.getText());
                boolean regle = regleCheckBox.isSelected();
                int idExamen = listeExamens.get(examenComboBox.getSelectedIndex()).getId();
                int idPatient = listePatients.get(patientComboBox.getSelectedIndex()).getIdPatient();

                // Création de l'objet Facture
                Facture facture = new Facture(date, montant, regle);
                facture.setIdFacture(id);
                facture.setIdExamen(idExamen);
                facture.setIdPatient(idPatient);

                // update de la facture et refresh
                factureService.updateFacture(facture);
                chargerToutesLesFactures();
                viderChamps();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une facture à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Supprimer une facture
    private void supprimerFacture() {
        int selectedRow = facturesTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int idFacture = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                Facture facture = new Facture();
                facture.setIdFacture(idFacture);

                // facture supprimée
                factureService.deleteFacture(facture);
                chargerToutesLesFactures();
                viderChamps();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une facture à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // remplit champ du formulaire avec la ligne sélectionnée
    private void remplirChampsDepuisTableau() {
        int selectedRow = facturesTable.getSelectedRow();
        if (selectedRow != -1) {
            montantField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            dateFactureField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            regleCheckBox.setSelected("Oui".equalsIgnoreCase(tableModel.getValueAt(selectedRow, 3).toString()));
            regleCheckBox.setEnabled(true);

            // Sélection de l'examen et du patient dans les JComboBox
            examenComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
            patientComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5).toString());
        }
    }

    // récupère les différentes données et les met dans la liste
    private void chargerToutesLesFactures() throws IOException, InterruptedException {
        tableModel.setRowCount(0);
        Factures factures = factureService.selectFactures();
        ArrayList<Facture> listeFactures = new ArrayList<>(factures.getFactures());

        for (Facture facture : listeFactures) {
            String nomExamen = trouverNomExamen(facture.getIdExamen());
            String nomPatient = trouverNomPatient(facture.getIdPatient());

            tableModel.addRow(new Object[]{
                    facture.getIdFacture(),
                    facture.getDateFacture(),
                    facture.getMontantFacture(),
                    facture.getRegle() ? "Oui" : "Non",
                    nomExamen,
                    nomPatient
            });
        }
    }

    // charge la liste des exams depuis le service
    private void chargerExamens() throws IOException, InterruptedException {
    
        Object selectionExamen = examenComboBox.getSelectedItem(); //sauvargde l'exam selectionné

        Examens examens = examenService.selectExamens();
        if (examens != null && examens.getExamens() != null) {
            listeExamens = new ArrayList<>(examens.getExamens());
            examenComboBox.removeAllItems();
            for (Examen examen : listeExamens) {
                examenComboBox.addItem(examen.getNom());
            }

            // si il y'avait une selection d'un exam, la remettre
            if (selectionExamen != null) {
                examenComboBox.setSelectedItem(selectionExamen);
            }
        }
    }

    

    // charge la liste des patients depuis le service
    private void chargerPatients() throws IOException, InterruptedException {
        Object selectionPatient = patientComboBox.getSelectedItem(); //idem

        Patients patients = patientService.selectPatients();
        if (patients != null && patients.getPatients() != null) {
            listePatients = new ArrayList<>(patients.getPatients());
            patientComboBox.removeAllItems();
            for (Patient patient : listePatients) {
                patientComboBox.addItem(patient.getNom());
            }

            // idem
            if (selectionPatient != null) {
                patientComboBox.setSelectedItem(selectionPatient);
            }
        }
    }

    // id donné puis compare avec les id des différents patient - si les deux id correspondent, retourne le nom de l'exam
    private String trouverNomExamen(int idExamen) {
        for (Examen examen : listeExamens) {
            if (examen.getId() == idExamen) {
                return examen.getNom();
            }
        }
        return "id examen non trouvé";
    }

    // id donné puis compare avec les id des différents patient - si les deux id correspondent, retourne le nom de l'exam
    private String trouverNomPatient(int idPatient) {
        for (Patient patient : listePatients) {
            if (patient.getIdPatient() == idPatient) {
                return patient.getNom();
            }
        }
        return "id patient non trouvé";
    }

    // champ formulaire réinitialisé

    private void viderChamps() {
        montantField.setText("");
        dateFactureField.setText("");
        regleCheckBox.setSelected(false);
        regleCheckBox.setEnabled(false);
        examenComboBox.setSelectedIndex(-1);
        patientComboBox.setSelectedIndex(-1);
        facturesTable.clearSelection();
    }
}