package edu.ezip.ing1.pds.graphics;

import java.awt.*;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.services.EquipementService;
import edu.ezip.ing1.pds.services.MaintenanceService;
import edu.ezip.ing1.pds.services.PaiementService;

public class RevenuesNetParMois extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private final PaiementService paiementService;
    private final EquipementService equipementService;
    private final MaintenanceService maintenanceService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    public RevenuesNetParMois() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.paiementService = new PaiementService(networkConfig);
        this.equipementService = new EquipementService(networkConfig);
        this.maintenanceService = new MaintenanceService(networkConfig);
        setLayout(new BorderLayout());


        String[] columns = {"Mois", "Total Paiements", "Coût Total", "Revenu Net"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JTextField moisField = new JTextField(10);
        JButton filterButton = new JButton("Filtrer");
        filterButton.addActionListener(e -> {
            try {
                YearMonth mois = YearMonth.parse(moisField.getText(), formatter);
                chargerRevenusPourMois(mois);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Format de mois invalide. Utilisez yyyy-MM.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton resetButton = new JButton("Réinitialiser");
        resetButton.addActionListener(e -> {
            try {
                chargerRevenusNetsParMois();
                moisField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Mois (yyyy-MM):"));
        bottomPanel.add(moisField);
        bottomPanel.add(filterButton);
        bottomPanel.add(resetButton);
        add(bottomPanel, BorderLayout.SOUTH);

        try {
            chargerRevenusNetsParMois();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur initiale: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void chargerRevenusNetsParMois() throws IOException, InterruptedException {
        model.setRowCount(0);

        Map<YearMonth, Double> paiementsParMois = new HashMap<>();
        Map<YearMonth, Double> coutEquipementParMois = new HashMap<>();
        Map<YearMonth, Double> coutMaintenanceParMois = new HashMap<>();
        Set<YearMonth> tousLesMois = new TreeSet<>();

        TotalPaiements paiements = paiementService.getTotalPaiementParJour();
        if (paiements != null && paiements.getTotalPaiements() != null) {
            for (TotalPaiement p : paiements.getTotalPaiements()) {
                YearMonth mois = YearMonth.from(p.getDatePaiement());
                paiementsParMois.merge(mois, p.getTotalPaiement(), Double::sum);
                tousLesMois.add(mois);
            }
        }

        TotalCouts couts = equipementService.getTotalCoutParJour();
        if (couts != null && couts.getTotalCouts() != null) {
            for (TotalCout tc : couts.getTotalCouts()) {
                YearMonth mois = YearMonth.from(tc.getDateAchat());
                coutEquipementParMois.merge(mois, tc.getTotalCout(), Double::sum);
                tousLesMois.add(mois);
            }
        }

        TotalMaintenances maintenances = maintenanceService.getTotalMaintenanceParJour();
        if (maintenances != null && maintenances.getTotalMaintenances() != null) {
            for (TotalMaintenance tm : maintenances.getTotalMaintenances()) {
                YearMonth mois = YearMonth.from(tm.getDateMaintenance());
                coutMaintenanceParMois.merge(mois, tm.getTotalMaintenance(), Double::sum);
                tousLesMois.add(mois);
            }
        }

        double totalPaiements = 0;
        double totalCouts = 0;
        int count = 0;

        for (YearMonth mois : tousLesMois) {
            double totalMoisPaiements = paiementsParMois.getOrDefault(mois, 0.0);
            double coutEq = coutEquipementParMois.getOrDefault(mois, 0.0);
            double coutMaint = coutMaintenanceParMois.getOrDefault(mois, 0.0);
            double coutTotal = coutEq + coutMaint;
            double revenuNet = totalMoisPaiements - coutTotal;

            model.addRow(new Object[]{
                    mois.toString(),
                    totalMoisPaiements,
                    coutTotal,
                    revenuNet
            });

            totalPaiements += totalMoisPaiements;
            totalCouts += coutTotal;
            count++;
        }

        if (count > 0) {
            double moyennePaiement = totalPaiements / count;
            double moyenneCout = totalCouts / count;
            double revenuNetMoyen = moyennePaiement - moyenneCout;
            YearMonth simulationMois = ((TreeSet<YearMonth>) tousLesMois).last().plusMonths(1);
            int ligneSimulation = model.getRowCount();
            model.addRow(new Object[]{
                    simulationMois.toString(),
                    moyennePaiement,
                    moyenneCout,
                    revenuNetMoyen
            });

            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (row == ligneSimulation) {
                        cell.setBackground(Color.CYAN);
                    } else {
                        cell.setBackground(Color.WHITE);
                    }
                    return cell;
                }
            });
        }
    }

    private void chargerRevenusPourMois(YearMonth mois) throws IOException, InterruptedException {
        model.setRowCount(0);

        double totalPaiement = 0.0;
        double coutEq = 0.0;
        double coutMaint = 0.0;

        TotalPaiements paiements = paiementService.getTotalPaiementParJour();
        if (paiements != null && paiements.getTotalPaiements() != null) {
            for (TotalPaiement p : paiements.getTotalPaiements()) {
                if (YearMonth.from(p.getDatePaiement()).equals(mois)) {
                    totalPaiement += p.getTotalPaiement();
                }
            }
        }

        TotalCouts couts = equipementService.getTotalCoutParJour();
        if (couts != null && couts.getTotalCouts() != null) {
            for (TotalCout tc : couts.getTotalCouts()) {
                if (YearMonth.from(tc.getDateAchat()).equals(mois)) {
                    coutEq += tc.getTotalCout();
                }
            }
        }

        TotalMaintenances maintenances = maintenanceService.getTotalMaintenanceParJour();
        if (maintenances != null && maintenances.getTotalMaintenances() != null) {
            for (TotalMaintenance tm : maintenances.getTotalMaintenances()) {
                if (YearMonth.from(tm.getDateMaintenance()).equals(mois)) {
                    coutMaint += tm.getTotalMaintenance();
                }
            }
        }

        double coutTotal = coutEq + coutMaint;
        double revenuNet = totalPaiement - coutTotal;

        model.addRow(new Object[]{
                mois.toString(),
                totalPaiement,
                coutTotal,
                revenuNet
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RevenuesNetParMois::new);
    }
}
