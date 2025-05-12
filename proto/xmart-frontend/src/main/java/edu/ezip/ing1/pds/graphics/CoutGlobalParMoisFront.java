package edu.ezip.ing1.pds.graphics;

import java.awt.*;
import java.io.IOException;
import java.time.YearMonth;
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

public class CoutGlobalParMoisFront {
    private DefaultTableModel model;
    private JTable table;
    private final EquipementService equipementService;
    private final MaintenanceService maintenanceService;
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

    public CoutGlobalParMoisFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.equipementService = new EquipementService(networkConfig);
        this.maintenanceService = new MaintenanceService(networkConfig);


        JFrame frame = new JFrame("Coût Global (par Mois)");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        String[] columns = {"Mois", "Coût Équipements", "Coût Maintenances", "Total Coût"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);


        JButton boutonActualiser = new JButton("Actualiser");
        boutonActualiser.addActionListener(e -> {
            try {
                chargerCoutsGlobauxParMois();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors du chargement: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelSud = new JPanel();
        panelSud.add(boutonActualiser);
        frame.add(panelSud, BorderLayout.SOUTH);

        try {
            chargerCoutsGlobauxParMois();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    private void chargerCoutsGlobauxParMois() throws IOException, InterruptedException {
        model.setRowCount(0); // Reset tableau

        Map<YearMonth, Double> coutEquipementParMois = new HashMap<>();
        Map<YearMonth, Double> coutMaintenanceParMois = new HashMap<>();
        Set<YearMonth> tousLesMois = new TreeSet<>();


        TotalCouts totalCouts = equipementService.getTotalCoutParJour();
        if (totalCouts != null && totalCouts.getTotalCouts() != null) {
            for (TotalCout tc : totalCouts.getTotalCouts()) {
                YearMonth mois = YearMonth.from(tc.getDateAchat());
                coutEquipementParMois.merge(mois, tc.getTotalCout(), Double::sum);
                tousLesMois.add(mois);
            }
        }


        TotalMaintenances totalMaintenances = maintenanceService.getTotalMaintenanceParJour();
        if (totalMaintenances != null && totalMaintenances.getTotalMaintenances() != null) {
            for (TotalMaintenance tm : totalMaintenances.getTotalMaintenances()) {
                YearMonth mois = YearMonth.from(tm.getDateMaintenance());
                coutMaintenanceParMois.merge(mois, tm.getTotalMaintenance(), Double::sum);
                tousLesMois.add(mois);
            }
        }


        for (YearMonth mois : tousLesMois) {
            double coutEq = coutEquipementParMois.getOrDefault(mois, 0.0);
            double coutMaint = coutMaintenanceParMois.getOrDefault(mois, 0.0);
            double total = coutEq + coutMaint;

            model.addRow(new Object[]{
                    mois.format(monthFormatter),
                    coutEq,
                    coutMaint,
                    total
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CoutGlobalParMoisFront::new);
    }
}
