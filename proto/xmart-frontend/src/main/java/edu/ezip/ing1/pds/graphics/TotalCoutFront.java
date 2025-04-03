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
import edu.ezip.ing1.pds.services.EquipementService;
import edu.ezip.ing1.pds.business.dto.TotalCouts;
import edu.ezip.ing1.pds.business.dto.TotalCout;

public class TotalCoutFront {
    private DefaultTableModel model;
    private JTable table;
    private final EquipementService equipementService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Déclarer le champ pour afficher l'estimation des coûts
    private JLabel estimationLabel;

    public TotalCoutFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.equipementService = new EquipementService(networkConfig);

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
                chargerTotalCouts();
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
            chargerTotalCouts();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des coûts: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    // Méthode pour charger les données dans le tableau
    private void chargerTotalCouts() throws IOException, InterruptedException {
        model.setRowCount(0); // Réinitialiser le tableau avant de le remplir

        TotalCouts totalCouts = equipementService.getTotalCoutParJour();
        if (totalCouts != null && totalCouts.getTotalCouts() != null) {
            // Trier par date d'achat
            totalCouts.getTotalCouts().sort(Comparator.comparing(TotalCout::getDateAchat));

            LocalDate lastDate = null;
            for (TotalCout tc : totalCouts.getTotalCouts()) {
                model.addRow(new Object[]{
                        tc.getDateAchat().format(formatter),
                        tc.getTotalCout()
                });
                lastDate = tc.getDateAchat();
            }

            // Si on a une dernière date, on ajoute une seule prévision pour le jour suivant
            if (lastDate != null) {
                double totalCoutMoyen = calculerCoutMoyen(totalCouts);
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
                        if (row == totalCouts.getTotalCouts().size()) {
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
    private double calculerCoutMoyen(TotalCouts totalCouts) {
        double total = 0;
        int count = 0;

        for (TotalCout tc : totalCouts.getTotalCouts()) {
            total += tc.getTotalCout();
            count++;
        }

        return count > 0 ? total / count : 0; // Retourne 0 si aucune donnée réelle
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TotalCoutFront::new);
    }
}
