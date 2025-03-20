package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Paiement;
import edu.ezip.ing1.pds.business.dto.Paiements;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.PaiementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class PaiementFront {
    private JTextField montantChamp, datePaiementChamp, moyenDePaiementChamp;
    private DefaultTableModel model;
    private JTable table;
    private final PaiementService paiementService;

    public PaiementFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.paiementService = new PaiementService(networkConfig);

        JFrame frame = new JFrame("Gestion des Paiements");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panelNord = new JPanel(new GridLayout(3, 2, 5, 5));

        montantChamp = new JTextField();
        datePaiementChamp = new JTextField();
        moyenDePaiementChamp = new JTextField();

        panelNord.add(new JLabel("Montant :"));
        panelNord.add(montantChamp);
        panelNord.add(new JLabel("Date de paiement :"));
        panelNord.add(datePaiementChamp);
        panelNord.add(new JLabel("Moyen de Paiement :"));
        panelNord.add(moyenDePaiementChamp);

        frame.add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID", "Montant", "Date de paiement", "Moyen de Paiement"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelSud = new JPanel();
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");

        panelSud.add(boutonAjouter);
        panelSud.add(boutonModifier);
        panelSud.add(boutonSupprimer);

        frame.add(panelSud, BorderLayout.SOUTH);

        boutonAjouter.addActionListener(e -> {
            try {
                String montantText = montantChamp.getText();
                String date = datePaiementChamp.getText();
                String moyen = moyenDePaiementChamp.getText();

                if (montantText.isEmpty() && date.isEmpty() && moyen.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Tous les champs doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double montant = Double.parseDouble(montantText);
                
                Paiement paiement = new Paiement();
                paiement.setmontant(montant);
                paiement.setdatePaiement(date);
                paiement.setmoyenDePaiement(moyen);

                paiementService.insertPaiement(paiement);
                chargerPaiements();
                viderChamps();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Le montant doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                montantChamp.setText(model.getValueAt(i, 1).toString());
                datePaiementChamp.setText(model.getValueAt(i, 2).toString());
                moyenDePaiementChamp.setText(model.getValueAt(i, 3).toString());
            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Paiement paiement = new Paiement();
                    paiement.setidPaiement(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    paiement.setmontant(Double.parseDouble(montantChamp.getText()));
                    paiement.setdatePaiement(datePaiementChamp.getText());
                    paiement.setmoyenDePaiement(moyenDePaiementChamp.getText());

                    paiementService.updatePaiement(paiement);
                    chargerPaiements();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonSupprimer.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Paiement paiement = new Paiement();
                    paiement.setidPaiement(Integer.parseInt(model.getValueAt(i, 0).toString()));

                    paiementService.deletePaiement(paiement);
                    chargerPaiements();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerPaiements();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des paiements: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    private void chargerPaiements() throws IOException, InterruptedException {
        model.setRowCount(0);
        Paiements paiements = paiementService.selectPaiements();
        if (paiements != null && paiements.getPaiements() != null) {
            for (Paiement p : paiements.getPaiements()) {
                model.addRow(new Object[]{
                    p.getidPaiement(),
                    p.getmontant(),
                    p.getdatePaiement(),
                    p.getmoyenDePaiement()
                });
            }
        }
    }

    private void viderChamps() {
        montantChamp.setText("");
        datePaiementChamp.setText("");
        moyenDePaiementChamp.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaiementFront::new);
    }
}
