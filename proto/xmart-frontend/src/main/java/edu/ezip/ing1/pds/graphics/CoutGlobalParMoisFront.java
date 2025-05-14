package edu.ezip.ing1.pds.graphics;

import java.awt.*;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

public class CoutGlobalParMoisFront extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private final EquipementService equipementService;
    private final MaintenanceService maintenanceService;
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private int ligneSimulation = -1;
    private JTextField moisField;

    public CoutGlobalParMoisFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.equipementService = new EquipementService(networkConfig);
        this.maintenanceService = new MaintenanceService(networkConfig);
        setLayout(new BorderLayout());


        String[] columns = {"Mois", "Coût Équipements", "Coût Maintenances", "Total Coût"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model) {
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (row == ligneSimulation) {
                    c.setBackground(new Color(173, 216, 230));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        add(new JScrollPane(table), BorderLayout.CENTER);

        moisField = new JTextField(7);
        JButton boutonFiltrer = new JButton("Filtrer");
        boutonFiltrer.addActionListener(e -> {
            try {
                YearMonth selectedMonth = YearMonth.parse(moisField.getText(), monthFormatter);
                chargerCoutsPourMois(selectedMonth);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Format de mois invalide. Utilisez yyyy-MM.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors du filtrage: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton boutonReinitialiser = new JButton("Réinitialiser");
        boutonReinitialiser.addActionListener(e -> {
            try {
                chargerCoutsGlobauxParMois();
                moisField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors du rechargement: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelSud = new JPanel();
        panelSud.add(new JLabel("Mois (yyyy-MM): "));
        panelSud.add(moisField);
        panelSud.add(boutonFiltrer);
        panelSud.add(boutonReinitialiser);
        add(panelSud, BorderLayout.SOUTH);

        try {
            chargerCoutsGlobauxParMois();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void chargerCoutsGlobauxParMois() throws IOException, InterruptedException {
        model.setRowCount(0);
        ligneSimulation = -1;

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

        double sommeEq = 0.0;
        double sommeMaint = 0.0;
        int count = 0;

        for (YearMonth mois : tousLesMois) {
            double coutEq = coutEquipementParMois.getOrDefault(mois, 0.0);
            double coutMaint = coutMaintenanceParMois.getOrDefault(mois, 0.0);
            double total = coutEq + coutMaint;

            sommeEq += coutEq;
            sommeMaint += coutMaint;
            count++;

            model.addRow(new Object[]{
                    mois.format(monthFormatter),
                    coutEq,
                    coutMaint,
                    total
            });
        }

        if (count > 0) {
            double moyenneEq = sommeEq / count;
            double moyenneMaint = sommeMaint / count;
            double total = moyenneEq + moyenneMaint;

            YearMonth dernierMois = ((TreeSet<YearMonth>) tousLesMois).last();
            YearMonth moisSuivant = dernierMois.plusMonths(1);

            ligneSimulation = model.getRowCount();
            model.addRow(new Object[]{
                    moisSuivant.format(monthFormatter),
                    moyenneEq,
                    moyenneMaint,
                    total
            });
        }
    }

    private void chargerCoutsPourMois(YearMonth mois) throws IOException, InterruptedException {
        model.setRowCount(0);
        ligneSimulation = -1;

        double coutEq = 0.0;
        double coutMaint = 0.0;

        TotalCouts totalCouts = equipementService.getTotalCoutParJour();
        if (totalCouts != null && totalCouts.getTotalCouts() != null) {
            for (TotalCout tc : totalCouts.getTotalCouts()) {
                if (YearMonth.from(tc.getDateAchat()).equals(mois)) {
                    coutEq += tc.getTotalCout();
                }
            }
        }

        TotalMaintenances totalMaintenances = maintenanceService.getTotalMaintenanceParJour();
        if (totalMaintenances != null && totalMaintenances.getTotalMaintenances() != null) {
            for (TotalMaintenance tm : totalMaintenances.getTotalMaintenances()) {
                if (YearMonth.from(tm.getDateMaintenance()).equals(mois)) {
                    coutMaint += tm.getTotalMaintenance();
                }
            }
        }

        double total = coutEq + coutMaint;

        model.addRow(new Object[]{
                mois.format(monthFormatter),
                coutEq,
                coutMaint,
                total
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CoutGlobalParMoisFront::new);
    }
}
