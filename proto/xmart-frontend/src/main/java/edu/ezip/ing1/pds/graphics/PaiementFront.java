package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Paiement;
import edu.ezip.ing1.pds.business.dto.Paiements;
import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.PaiementService;
import edu.ezip.ing1.pds.services.FactureService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


// déclaration des composants de l'ihm
public class PaiementFront extends JPanel {
    private JTextField montantField, datePaiementField;
    private JRadioButton carteBancaireRadio, especesRadio, chequeRadio, tiersPayantRadio;
    private ButtonGroup moyenDePaiementGroup;
    private JComboBox<Integer> idFactureComboBox;
    private DefaultTableModel tableModel;
    private JTable paiementsTable;

    // Services
    private final PaiementService paiementService;
    private final FactureService factureService;

        // Données
    private ArrayList<Facture> listeFactures;

    public PaiementFront() throws InterruptedException, IOException {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.factureService = new FactureService(networkConfig);
        this.paiementService = new PaiementService(networkConfig);
 
        setLayout(new BorderLayout());
        setSize(700, 400);
 
        JPanel panelNord = new JPanel(new GridLayout(4, 2, 5, 5));

        // Initialisation des composants
        listeFactures = new ArrayList<>();
        idFactureComboBox = new JComboBox<>();
        montantField = new JTextField();
        datePaiementField = new JTextField();
       
        // Radio-boutons pour le moyen de paiement
        carteBancaireRadio = new JRadioButton("Carte Bancaire");
        especesRadio = new JRadioButton("Espèces");
        chequeRadio = new JRadioButton("Chèque");
        tiersPayantRadio = new JRadioButton("Tiers-Payant");

        moyenDePaiementGroup = new ButtonGroup();
        moyenDePaiementGroup.add(carteBancaireRadio);
        moyenDePaiementGroup.add(especesRadio);
        moyenDePaiementGroup.add(chequeRadio);
        moyenDePaiementGroup.add(tiersPayantRadio);

        JPanel radioPanel = new JPanel(new GridLayout(2, 2));
        radioPanel.add(carteBancaireRadio);
        radioPanel.add(especesRadio);
        radioPanel.add(chequeRadio);
        radioPanel.add(tiersPayantRadio);
 
        panelNord.add(new JLabel("ID Facture :"));
        panelNord.add(idFactureComboBox);
        panelNord.add(new JLabel("Montant :"));
        panelNord.add(montantField);
        panelNord.add(new JLabel("Date de paiement (AAAA-MM-DD) :"));
        panelNord.add(datePaiementField);
        panelNord.add(new JLabel("Moyen de Paiement :"));
        panelNord.add(radioPanel);
 
        add(panelNord, BorderLayout.NORTH);


        // tableau des paiements
        String[] columns = {"ID", "Montant", "Date de paiement", "Moyen de Paiement", "ID Facture"};
        tableModel = new DefaultTableModel(columns, 0);
        paiementsTable = new JTable(tableModel);
        add(new JScrollPane(paiementsTable), BorderLayout.CENTER);

        // bouton soumettre
        JPanel panelSud = new JPanel();
        JButton boutonSoumettre = new JButton("Soumettre");
        panelSud.add(boutonSoumettre);
        add(panelSud, BorderLayout.SOUTH);

        boutonSoumettre.addActionListener(e -> soumettrePaiement());
 
        // Chargement  des données
        chargerDonnees();


        // Maj liste des factures
        Timer timer = new Timer(30000, e -> {
            try {
                chargerFactures();
            } catch (Exception ex) {
                System.err.println("Erreur de mise à jour : " + ex.getMessage());
            }});

        timer.start();
    }

    // chargement factures 
    private void chargerDonnees() {
            chargerFactures();
            chargerPaiements();
    }
    

    // Nouveau paiement

    private void soumettrePaiement() {
        try {
            // il faut remplir les champs
            if (montantField.getText().isEmpty() && datePaiementField.getText().isEmpty() && getMoyenDePaiementSelectionne() == null) {
                JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double montant = Double.parseDouble(montantField.getText());
            LocalDate datePaiement = LocalDate.parse(datePaiementField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String moyen = getMoyenDePaiementSelectionne();
            int idFacture = (int) idFactureComboBox.getSelectedItem();

            // Confirmation de paiement
            int confirmation = JOptionPane.showConfirmDialog(this, "Confirmer ce paiement ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                Paiement paiement = new Paiement();
                paiement.setmontant(montant);
                paiement.setdatePaiement(datePaiement);
                paiement.setmoyenDePaiement(moyen);
                paiement.setidFacture(idFacture);

                paiementService.insertPaiement(paiement);
                chargerPaiements();
                viderChamps();
                JOptionPane.showMessageDialog(this, "Paiement ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le montant doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide (AAAA-MM-DD).", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // récupère le moyen de pauement selectionné
    private String getMoyenDePaiementSelectionne() {
        if (carteBancaireRadio.isSelected()) 
        return "Carte Bancaire";
        if (especesRadio.isSelected()) 
        return "Espèces";
        if (chequeRadio.isSelected()) 
        return "Chèque";
        if (tiersPayantRadio.isSelected()) 
        return "Tiers-Payant";
        
        return null;
    }

    // charger la liste des paiements
         private void chargerPaiements() {
          try {
        tableModel.setRowCount(0);
        Paiements paiements = paiementService.selectPaiements();
        if (paiements != null && paiements.getPaiements() != null) {
        for (Paiement p : paiements.getPaiements()) {
            tableModel.addRow(new Object[]{
                p.getidPaiement(),
                p.getmontant(),
                p.getdatePaiement(),
                p.getmoyenDePaiement(),
                p.getidFacture()
                });
            }
        }
    } catch (IOException | InterruptedException e) {
        JOptionPane.showMessageDialog(this, "Erreur de chargement des paiements.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    }


    // chargement des idfacture dans la JCombo 
    private void chargerFactures() {
        try {
        Factures factures = factureService.selectFactures();
        if (factures != null && factures.getFactures() != null) {
            Object selectionFacture = idFactureComboBox.getSelectedItem();
            listeFactures = new ArrayList<>(factures.getFactures());
            
            idFactureComboBox.removeAllItems();
            for (Facture f : listeFactures) {
                idFactureComboBox.addItem(f.getIdFacture());
            }
            if (selectionFacture != null) {
                idFactureComboBox.setSelectedItem(selectionFacture);
            }
        }
    } catch (IOException | InterruptedException e) {
        System.err.println("Erreur de chargement des factures : " + e.getMessage());
    }
}

    // réinitialise les champs
    private void viderChamps() {
        montantField.setText("");
        datePaiementField.setText("");
        moyenDePaiementGroup.clearSelection();
        idFactureComboBox.setSelectedIndex(-1);
    }
}