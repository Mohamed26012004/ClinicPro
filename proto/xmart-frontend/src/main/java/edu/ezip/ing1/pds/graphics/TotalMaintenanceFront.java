package edu.ezip.ing1.pds.graphics;


import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.MaintenanceService;
import edu.ezip.ing1.pds.business.dto.TotalMaintenances;
import edu.ezip.ing1.pds.business.dto.TotalMaintenance;


public class TotalMaintenanceFront {
    private DefaultTableModel model;
    private JTable table;
    private final MaintenanceService maintenanceService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Déclarer le champ pour afficher l'estimation des coûts
    private JLabel estimationLabel;

    public TotalMaintenanceFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.maintenanceService = new MaintenanceService(networkConfig);

        // Création de la fenêtre
        JFrame frame = new JFrame("Coût Total par Jour");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Création du tableau
        String[] columns = {"Date Achat", "Total Coût"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // Bouton pour actualiser
        JButton boutonActualiser = new JButton("Actualiser");
        boutonActualiser.addActionListener(e -> {
            try {
                chargerTotalMaintenances();
                frame.revalidate();
                frame.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des coûts: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelSud = new JPanel();
        panelSud.add(boutonActualiser);
        frame.add(panelSud, BorderLayout.SOUTH);

        try {
            chargerTotalMaintenances();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des coûts: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    // Méthode pour charger les données dans le tableau
    private void chargerTotalMaintenances() throws IOException, InterruptedException {
        model.setRowCount(0); // Réinitialiser le tableau avant de le remplir

        TotalMaintenances totalMaintenances = maintenanceService.getTotalMaintenanceParJour();
        if (totalMaintenances != null && totalMaintenances.getTotalMaintenances() != null) {
            // Trier par date d'achat
            totalMaintenances.getTotalMaintenances().sort(Comparator.comparing(TotalMaintenance::getDateMaintenance));

            LocalDate lastDate = null;
            for (TotalMaintenance tc : totalMaintenances.getTotalMaintenances()) {
                model.addRow(new Object[]{
                        tc.getDateMaintenance().format(formatter),
                        tc.getTotalMaintenance()
                });
                lastDate = tc.getDateMaintenance();
            }

            // Si on a une dernière date, on ajoute une seule prévision pour le jour suivant
            if (lastDate != null) {
                double totalCoutMoyen = calculerCoutMoyen(totalMaintenances);
                LocalDate nextDay = lastDate.plusDays(1);
                model.addRow(new Object[]{
                        nextDay.format(formatter),
                        totalCoutMoyen
                });

                // Appliquer une couleur bleue à la ligne estimée
                table.setRowSelectionAllowed(false);
                table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                                            boolean hasFocus, int row, int column) {
                        java.awt.Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if (row == totalMaintenances.getTotalMaintenances().size()) {
                            cell.setBackground(Color.CYAN); // Couleur bleue pour la ligne estimée
                        } else {
                            cell.setBackground(Color.WHITE); // Couleur blanche pour les données réelles
                        }
                        return cell;
                    }
                });
            }
        }
    }


    // Calcul du coût moyen des données réelles pour l'estimation
    private double calculerCoutMoyen(TotalMaintenances totalMaintenances) {
        double total = 0;
        int count = 0;

        for (TotalMaintenance tc : totalMaintenances.getTotalMaintenances()) {
            total += tc.getTotalMaintenance();
            count++;
        }

        return count > 0 ? total / count : 0; // Retourne 0 si aucune donnée réelle
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TotalCoutFront::new);
    }
}


