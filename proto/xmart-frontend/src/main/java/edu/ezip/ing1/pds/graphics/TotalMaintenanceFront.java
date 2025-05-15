package edu.ezip.ing1.pds.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.MaintenanceService;
import edu.ezip.ing1.pds.business.dto.TotalMaintenances;
import edu.ezip.ing1.pds.business.dto.TotalMaintenance;

public class TotalMaintenanceFront extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private final MaintenanceService maintenanceService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private JTextField dateField;

    public TotalMaintenanceFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.maintenanceService = new MaintenanceService(networkConfig);
        setLayout(new BorderLayout());


        String[] columns = {"Date Maintenance", "Total Coût"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        dateField = new JTextField(10);

        JButton boutonAfficherParDate = new JButton("Filtrer");
        boutonAfficherParDate.addActionListener(e -> {
            try {
                LocalDate selectedDate = LocalDate.parse(dateField.getText(), formatter);
                chargerCoutsPourDate(selectedDate);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Format de date invalide. Utilisez yyyy-MM-dd.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors du filtrage: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton boutonReinitialiser = new JButton("Réinitialiser");
        boutonReinitialiser.addActionListener(e -> {
            try {
                chargerTotalMaintenances();
                revalidate();
                repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors du rechargement des données: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelSud = new JPanel();
        panelSud.add(new JLabel("Date (yyyy-MM-dd): "));
        panelSud.add(dateField);
        panelSud.add(boutonAfficherParDate);
        panelSud.add(boutonReinitialiser);

        add(panelSud, BorderLayout.SOUTH);

        try {
            chargerTotalMaintenances();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur initiale: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void chargerTotalMaintenances() throws IOException, InterruptedException {
        model.setRowCount(0);

        TotalMaintenances totalMaintenances = maintenanceService.getTotalMaintenanceParJour();
        if (totalMaintenances != null && totalMaintenances.getTotalMaintenances() != null) {

            totalMaintenances.getTotalMaintenances().sort(Comparator.comparing(TotalMaintenance::getDateMaintenance));

            LocalDate lastDate = null;
            for (TotalMaintenance tm : totalMaintenances.getTotalMaintenances()) {
                model.addRow(new Object[]{tm.getDateMaintenance().format(formatter), tm.getTotalMaintenance()});
                lastDate = tm.getDateMaintenance();
            }

            if (lastDate != null) {
                double totalCoutMoyen = calculerCoutMoyen(totalMaintenances);
                LocalDate nextDay = lastDate.plusDays(1);
                model.addRow(new Object[]{nextDay.format(formatter), totalCoutMoyen});

                table.setRowSelectionAllowed(false);
                table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                                            boolean hasFocus, int row, int column) {
                        java.awt.Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if (row == totalMaintenances.getTotalMaintenances().size()) {
                            cell.setBackground(Color.CYAN);
                        } else {
                            cell.setBackground(Color.WHITE);
                        }
                        return cell;
                    }
                });
            }
        }
    }

    private void chargerCoutsPourDate(LocalDate date) throws IOException, InterruptedException {
        model.setRowCount(0);
        TotalMaintenances totalMaintenances = maintenanceService.getTotalMaintenanceParJour();
        for (TotalMaintenance tm : totalMaintenances.getTotalMaintenances()) {
            if (tm.getDateMaintenance().equals(date)) {
                model.addRow(new Object[]{tm.getDateMaintenance().format(formatter), tm.getTotalMaintenance()});
            }
        }
    }

    private double calculerCoutMoyen(TotalMaintenances totalMaintenances) {
        double total = 0;
        int count = 0;

        for (TotalMaintenance tm : totalMaintenances.getTotalMaintenances()) {
            total += tm.getTotalMaintenance();
            count++;
        }

        return count > 0 ? total / count : 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TotalMaintenanceFront::new);
    }
}