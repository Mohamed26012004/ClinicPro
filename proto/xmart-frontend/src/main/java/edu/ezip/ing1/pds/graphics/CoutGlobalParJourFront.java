package edu.ezip.ing1.pds.graphics;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.EquipementService;
import edu.ezip.ing1.pds.services.MaintenanceService;
import edu.ezip.ing1.pds.business.dto.TotalCouts;
import edu.ezip.ing1.pds.business.dto.TotalCout;
import edu.ezip.ing1.pds.business.dto.TotalMaintenances;
import edu.ezip.ing1.pds.business.dto.TotalMaintenance;

public class CoutGlobalParJourFront {
    private DefaultTableModel model;
    private JTable table;
    private final EquipementService equipementService;
    private final MaintenanceService maintenanceService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CoutGlobalParJourFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.equipementService = new EquipementService(networkConfig);
        this.maintenanceService = new MaintenanceService(networkConfig);


        JFrame frame = new JFrame("Coût Global (Équipements + Maintenances) par Jour");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        String[] columns = {"Date", "Coût Équipements", "Coût Maintenances", "Total Coût"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);


        JButton boutonActualiser = new JButton("Actualiser");
        boutonActualiser.addActionListener(e -> {
            try {
                chargerCoutsGlobaux();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors du chargement: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelSud = new JPanel();
        panelSud.add(boutonActualiser);
        frame.add(panelSud, BorderLayout.SOUTH);

        try {
            chargerCoutsGlobaux();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    private void chargerCoutsGlobaux() throws IOException, InterruptedException {
        model.setRowCount(0); // Reset tableau

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


        for (LocalDate date : toutesLesDates) {
            double coutEq = coutEquipementParJour.getOrDefault(date, 0.0);
            double coutMaint = coutMaintenanceParJour.getOrDefault(date, 0.0);
            double total = coutEq + coutMaint;

            model.addRow(new Object[]{
                    date.format(formatter),
                    coutEq,
                    coutMaint,
                    total
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CoutGlobalParJourFront::new);
    }
}
