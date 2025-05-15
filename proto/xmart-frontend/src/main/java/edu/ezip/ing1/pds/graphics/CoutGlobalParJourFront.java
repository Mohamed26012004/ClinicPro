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
import edu.ezip.ing1.pds.services.EquipementService;
import edu.ezip.ing1.pds.services.MaintenanceService;
import edu.ezip.ing1.pds.business.dto.TotalCouts;
import edu.ezip.ing1.pds.business.dto.TotalCout;
import edu.ezip.ing1.pds.business.dto.TotalMaintenances;
import edu.ezip.ing1.pds.business.dto.TotalMaintenance;

public class CoutGlobalParJourFront extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private final EquipementService equipementService;
    private final MaintenanceService maintenanceService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private JTextField dateField;
    private int ligneSimulation = -1;

    public CoutGlobalParJourFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.equipementService = new EquipementService(networkConfig);
        this.maintenanceService = new MaintenanceService(networkConfig);
        setLayout(new BorderLayout());



        String[] columns = {"Date", "Coût Équipements", "Coût Maintenances", "Total Coût"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model) {
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (row == ligneSimulation) {
                    c.setBackground(Color.CYAN);
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        add(new JScrollPane(table), BorderLayout.CENTER);

        dateField = new JTextField(10);

        JButton boutonFiltrer = new JButton("Filtrer");
        boutonFiltrer.addActionListener(e -> {
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
                chargerCoutsGlobaux();
                dateField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors du rechargement: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelSud = new JPanel();
        panelSud.add(new JLabel("Date (yyyy-MM-dd): "));
        panelSud.add(dateField);
        panelSud.add(boutonFiltrer);
        panelSud.add(boutonReinitialiser);

        add(panelSud, BorderLayout.SOUTH);

        try {
            chargerCoutsGlobaux();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur initiale: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void chargerCoutsGlobaux() throws IOException, InterruptedException {
        model.setRowCount(0);
        ligneSimulation = -1;

        Map<LocalDate, Double> coutEquipementParJour = new HashMap<>();
        Map<LocalDate, Double> coutMaintenanceParJour = new HashMap<>();
        Set<LocalDate> toutesLesDates = new TreeSet<>();

        TotalCouts totalCouts = equipementService.getTotalCoutParJour();
        if (totalCouts != null && totalCouts.getTotalCouts() != null) {
            for (TotalCout tc : totalCouts.getTotalCouts()) {
                LocalDate date = tc.getDateAchat();
                coutEquipementParJour.put(date, tc.getTotalCout());
                toutesLesDates.add(date);
            }
        }

        TotalMaintenances totalMaintenances = maintenanceService.getTotalMaintenanceParJour();
        if (totalMaintenances != null && totalMaintenances.getTotalMaintenances() != null) {
            for (TotalMaintenance tm : totalMaintenances.getTotalMaintenances()) {
                LocalDate date = tm.getDateMaintenance();
                coutMaintenanceParJour.put(date, tm.getTotalMaintenance());
                toutesLesDates.add(date);
            }
        }

        double sommeEq = 0.0;
        double sommeMaint = 0.0;
        int count = 0;

        for (LocalDate date : toutesLesDates) {
            double coutEq = coutEquipementParJour.getOrDefault(date, 0.0);
            double coutMaint = coutMaintenanceParJour.getOrDefault(date, 0.0);
            double total = coutEq + coutMaint;

            sommeEq += coutEq;
            sommeMaint += coutMaint;
            count++;

            model.addRow(new Object[]{
                    date.format(formatter),
                    coutEq,
                    coutMaint,
                    total
            });
        }

        if (count > 0) {
            double moyenneEq = sommeEq / count;
            double moyenneMaint = sommeMaint / count;
            double total = moyenneEq + moyenneMaint;

            LocalDate maxDate = ((TreeSet<LocalDate>) toutesLesDates).last();
            LocalDate dateSimulation = maxDate.plusDays(1);

            ligneSimulation = model.getRowCount();
            model.addRow(new Object[]{
                    dateSimulation.format(formatter),
                    moyenneEq,
                    moyenneMaint,
                    total
            });
        }
    }

    private void chargerCoutsPourDate(LocalDate date) throws IOException, InterruptedException {
        model.setRowCount(0);
        ligneSimulation = -1;

        double coutEq = 0.0;
        double coutMaint = 0.0;

        TotalCouts totalCouts = equipementService.getTotalCoutParJour();
        if (totalCouts != null && totalCouts.getTotalCouts() != null) {
            for (TotalCout tc : totalCouts.getTotalCouts()) {
                if (tc.getDateAchat().equals(date)) {
                    coutEq = tc.getTotalCout();
                    break;
                }
            }
        }

        TotalMaintenances totalMaintenances = maintenanceService.getTotalMaintenanceParJour();
        if (totalMaintenances != null && totalMaintenances.getTotalMaintenances() != null) {
            for (TotalMaintenance tm : totalMaintenances.getTotalMaintenances()) {
                if (tm.getDateMaintenance().equals(date)) {
                    coutMaint = tm.getTotalMaintenance();
                    break;
                }
            }
        }

        double total = coutEq + coutMaint;

        model.addRow(new Object[]{
                date.format(formatter),
                coutEq,
                coutMaint,
                total
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CoutGlobalParJourFront::new);
    }
}
