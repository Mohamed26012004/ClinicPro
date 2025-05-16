package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Paiement;
import edu.ezip.ing1.pds.business.dto.Paiements;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.PaiementService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TotalPaiementFront extends  JPanel {
    private DefaultTableModel model;
    private JTable table;
    private final PaiementService paiementService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private JTextField dateField;

    public TotalPaiementFront () {

        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.paiementService = new PaiementService(networkConfig);
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new FlowLayout());
        p.add(Fenetre.createLabel("Les Revenus Quotidiens"));
        add(p, BorderLayout.NORTH);
        String[] colonnes = {"Date de Paiement", "Montant Total"};
        model = new DefaultTableModel(colonnes, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);


        dateField = new JTextField(10);


        JButton boutonAfficher = new JButton("Filtrer");
        boutonAfficher.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText(), formatter);
                chargerPaiementsPourDate(date);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Date invalide. Utilisez le format yyyy-MM-dd.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });


        JButton boutonReinitialiser = new JButton("Réinitialiser");
        boutonReinitialiser.addActionListener(e -> {
            try {
                chargerPaiementsTotaux();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });


        JPanel panelSud = new JPanel();
        panelSud.add(new JLabel("Date (yyyy-MM-dd) :"));
        panelSud.add(dateField);
        panelSud.add(boutonAfficher);
        panelSud.add(boutonReinitialiser);
        add(panelSud, BorderLayout.SOUTH);

        // Charger les données initiales
        try {
            chargerPaiementsTotaux();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }

    // charger les paiements  par date
    private void chargerPaiementsTotaux() throws IOException, InterruptedException {
        model.setRowCount(0);
        Paiements paiements = paiementService.selectPaiements();

        if (paiements != null && paiements.getPaiements() != null) {
            Map<LocalDate, Double> montantParDate = new TreeMap<>();
            for (Paiement p : paiements.getPaiements()) {
                montantParDate.merge(p.getdatePaiement(), p.getmontant(), Double::sum);
            }

            LocalDate lastDate = null;
            for (Map.Entry<LocalDate, Double> entry : montantParDate.entrySet()) {
                model.addRow(new Object[]{entry.getKey().format(formatter), entry.getValue()});
                lastDate = entry.getKey();
            }

            // Ajouter la moyenne
            if (lastDate != null) {
                double moyenne = calculerMontantMoyen(paiements);
                LocalDate dateMoyenne = lastDate.plusDays(1);
                model.addRow(new Object[]{dateMoyenne.format(formatter), moyenne});


                table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                                   boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if (row == montantParDate.size()) {
                            c.setBackground(Color.CYAN);
                        } else {
                            c.setBackground(Color.WHITE);
                        }
                        return c;
                    }
                });
            }
        }
    }

    // Méthode pour filtrer les paiements par date
    private void chargerPaiementsPourDate(LocalDate date) throws IOException, InterruptedException {
        model.setRowCount(0);
        Paiements paiements = paiementService.selectPaiements();

        double total = 0;
        for (Paiement p : paiements.getPaiements()) {
            if (p.getdatePaiement().equals(date)) {
                total += p.getmontant();
            }
        }

        if (total > 0) {
            model.addRow(new Object[]{date.format(formatter), total});
        } else {
            JOptionPane.showMessageDialog(null, "Aucun paiement trouvé pour cette date.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // calculer le montant moyen
    private double calculerMontantMoyen(Paiements paiements) {
        double total = 0;
        int count = 0;

        for (Paiement p : paiements.getPaiements()) {
            total += p.getmontant();
            count++;
        }

        return count > 0 ? total / count : 0;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(TotalPaiementFront::new);
    }
}
