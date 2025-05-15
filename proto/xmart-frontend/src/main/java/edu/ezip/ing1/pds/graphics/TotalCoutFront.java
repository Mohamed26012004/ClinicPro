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
import edu.ezip.ing1.pds.services.EquipementService;
import edu.ezip.ing1.pds.business.dto.TotalCouts;
import edu.ezip.ing1.pds.business.dto.TotalCout;

public class TotalCoutFront extends JPanel{
    private DefaultTableModel model;
    private JTable table;
    private final EquipementService equipementService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private JTextField dateField;

    public TotalCoutFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.equipementService = new EquipementService(networkConfig);
        setLayout((new BorderLayout()));



        String[] columns = {"Date Achat", "Total Coût"};
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
                chargerTotalCouts();
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
            chargerTotalCouts();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur initiale: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void chargerTotalCouts() throws IOException, InterruptedException {
        model.setRowCount(0);

        TotalCouts totalCouts = equipementService.getTotalCoutParJour();
        if (totalCouts != null && totalCouts.getTotalCouts() != null) {

            totalCouts.getTotalCouts().sort(Comparator.comparing(TotalCout::getDateAchat));

            LocalDate lastDate = null;
            for (TotalCout tc : totalCouts.getTotalCouts()) {
                model.addRow(new Object[]{tc.getDateAchat().format(formatter), tc.getTotalCout()});
                lastDate = tc.getDateAchat();
            }

            if (lastDate != null) {
                double totalCoutMoyen = calculerCoutMoyen(totalCouts);
                LocalDate nextDay = lastDate.plusDays(1);
                model.addRow(new Object[]{nextDay.format(formatter), totalCoutMoyen});

                table.setRowSelectionAllowed(false);
                table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                                            boolean hasFocus, int row, int column) {
                        java.awt.Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if (row == totalCouts.getTotalCouts().size()) {
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
        TotalCouts totalCouts = equipementService.getTotalCoutParJour();
        for (TotalCout tc : totalCouts.getTotalCouts()) {
            if (tc.getDateAchat().equals(date)) {
                model.addRow(new Object[]{tc.getDateAchat().format(formatter), tc.getTotalCout()});
            }
        }
    }

    private double calculerCoutMoyen(TotalCouts totalCouts) {
        double total = 0;
        int count = 0;

        for (TotalCout tc : totalCouts.getTotalCouts()) {
            total += tc.getTotalCout();
            count++;
        }

        return count > 0 ? total / count : 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TotalCoutFront::new);
    }
}
