package edu.ezip.ing1.pds.graphics;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
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

public class RevenuesNetParJour extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private final PaiementService paiementService;
    private final EquipementService equipementService;
    private final MaintenanceService maintenanceService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private JTextField dateField;
    private int ligneSimulation = -1;

    public RevenuesNetParJour() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.paiementService = new PaiementService(networkConfig);
        this.equipementService = new EquipementService(networkConfig);
        this.maintenanceService = new MaintenanceService(networkConfig);
        setLayout(new BorderLayout());


        JPanel j = new JPanel(new FlowLayout());
        j.add(Fenetre.createLabel("Les Revenus Nets Quotidiens"));
        add(j, BorderLayout.NORTH);
        String[] columns = {"Date", "Total Paiements", "Coût Total", "Revenu Net"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        dateField = new JTextField(10);
        JButton filterButton = new JButton("Filtrer");
        filterButton.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText(), formatter);
                chargerRevenusPourDate(date);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Format de date invalide. Utilisez yyyy-MM-dd.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton resetButton = new JButton("Réinitialiser");
        resetButton.addActionListener(e -> {
            try {
                chargerRevenusNets();
                dateField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        bottomPanel.add(dateField);
        bottomPanel.add(filterButton);
        bottomPanel.add(resetButton);

        add(bottomPanel, BorderLayout.SOUTH);

        try {
            chargerRevenusNets();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur initiale: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void chargerRevenusNets() throws IOException, InterruptedException {
        model.setRowCount(0);
        ligneSimulation = -1;

        Map<LocalDate, Double> paiementsParJour = new HashMap<>();
        Map<LocalDate, Double> coutEquipementParJour = new HashMap<>();
        Map<LocalDate, Double> coutMaintenanceParJour = new HashMap<>();
        Set<LocalDate> toutesLesDates = new TreeSet<>();

        TotalPaiements paiements = paiementService.getTotalPaiementParJour();
        if (paiements != null && paiements.getTotalPaiements() != null) {
            for (TotalPaiement p : paiements.getTotalPaiements()) {
                LocalDate date = p.getDatePaiement();
                paiementsParJour.merge(date, p.getTotalPaiement(), Double::sum);
                toutesLesDates.add(date);
            }
        }

        TotalCouts totalCoutsObj = equipementService.getTotalCoutParJour();
        if (totalCoutsObj != null && totalCoutsObj.getTotalCouts() != null) {
            for (TotalCout tc : totalCoutsObj.getTotalCouts()) {
                coutEquipementParJour.merge(tc.getDateAchat(), tc.getTotalCout(), Double::sum);
                toutesLesDates.add(tc.getDateAchat());
            }
        }

        TotalMaintenances totalMaintenances = maintenanceService.getTotalMaintenanceParJour();
        if (totalMaintenances != null && totalMaintenances.getTotalMaintenances() != null) {
            for (TotalMaintenance tm : totalMaintenances.getTotalMaintenances()) {
                coutMaintenanceParJour.merge(tm.getDateMaintenance(), tm.getTotalMaintenance(), Double::sum);
                toutesLesDates.add(tm.getDateMaintenance());
            }
        }

        double totalPaiements = 0;
        double totalCoutsMontant = 0;
        int count = 0;

        for (LocalDate date : toutesLesDates) {
            double paiement = paiementsParJour.getOrDefault(date, 0.0);
            double coutEq = coutEquipementParJour.getOrDefault(date, 0.0);
            double coutMaint = coutMaintenanceParJour.getOrDefault(date, 0.0);
            double coutTotal = coutEq + coutMaint;
            double revenuNet = paiement - coutTotal;

            model.addRow(new Object[]{
                    date.format(formatter),
                    paiement,
                    coutTotal,
                    revenuNet
            });

            totalPaiements += paiement;
            totalCoutsMontant += coutTotal;
            count++;
        }

        if (count > 0) {
            double moyennePaiement = totalPaiements / count;
            double moyenneCout = totalCoutsMontant / count;
            double revenuNetMoyen = moyennePaiement - moyenneCout;

            LocalDate simulationDate = ((TreeSet<LocalDate>) toutesLesDates).last().plusDays(1);
            ligneSimulation = model.getRowCount();
            model.addRow(new Object[]{
                    simulationDate.format(formatter),
                    moyennePaiement,
                    moyenneCout,
                    revenuNetMoyen
            });

            table.setRowSelectionAllowed(false);
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
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

    private void chargerRevenusPourDate(LocalDate date) throws IOException, InterruptedException {
        model.setRowCount(0);
        ligneSimulation = -1;

        double totalPaiement = 0.0;
        double coutEq = 0.0;
        double coutMaint = 0.0;

        TotalPaiements paiements = paiementService.getTotalPaiementParJour();
        if (paiements != null && paiements.getTotalPaiements() != null) {
            for (TotalPaiement p : paiements.getTotalPaiements()) {
                if (p.getDatePaiement().equals(date)) {
                    totalPaiement += p.getTotalPaiement();
                }
            }
        }

        TotalCouts totalCoutsObj = equipementService.getTotalCoutParJour();
        if (totalCoutsObj != null && totalCoutsObj.getTotalCouts() != null) {
            for (TotalCout tc : totalCoutsObj.getTotalCouts()) {
                if (tc.getDateAchat().equals(date)) {
                    coutEq += tc.getTotalCout();
                }
            }
        }

        TotalMaintenances totalMaintenances = maintenanceService.getTotalMaintenanceParJour();
        if (totalMaintenances != null && totalMaintenances.getTotalMaintenances() != null) {
            for (TotalMaintenance tm : totalMaintenances.getTotalMaintenances()) {
                if (tm.getDateMaintenance().equals(date)) {
                    coutMaint += tm.getTotalMaintenance();
                }
            }
        }

        double coutTotal = coutEq + coutMaint;
        double revenuNet = totalPaiement - coutTotal;

        model.addRow(new Object[]{
                date.format(formatter),
                totalPaiement,
                coutTotal,
                revenuNet
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RevenuesNetParJour::new);
    }
}
