package edu.ezip.ing1.pds.graphics;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.business.dto.Maintenance;
import edu.ezip.ing1.pds.business.dto.Maintenances;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.MaintenanceService;

public class MaintenanceFront extends JPanel{
    private JTextField idMaintenanceChamp, coutMaintenancechamp, typeMaintenancechamp, dateMaintenanceChamp, filtreDateChamp;
    private DefaultTableModel model;
    private JTable table;
    private final MaintenanceService maintenanceService;
    private final DateTimeFormatter formattage = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MaintenanceFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.maintenanceService = new MaintenanceService(networkConfig);
        setLayout(new BorderLayout());

        JPanel panelNord = new JPanel(new GridLayout(5, 2, 5, 5));

        idMaintenanceChamp = new JTextField();
        coutMaintenancechamp = new JTextField();
        typeMaintenancechamp = new JTextField();
        typeMaintenancechamp.setEditable(false);
        dateMaintenanceChamp = new JTextField();
        filtreDateChamp = new JTextField();


        String[] typesMaintenancePredefinis = {
                "Nettoyage quotidien",
                "Désinfection régulière",
                "Vérification d’équipements",
                "Contrôle électrique",
                "Entretien du mobilier",
                "Gestion des déchets"
        };
        JList<String> listeTypes = new JList<>(typesMaintenancePredefinis);
        JScrollPane scrollPane = new JScrollPane(listeTypes);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        JPopupMenu popupType = new JPopupMenu();
        popupType.setLayout(new BorderLayout());
        popupType.add(scrollPane, BorderLayout.CENTER);

        typeMaintenancechamp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                popupType.show(typeMaintenancechamp, 0, typeMaintenancechamp.getHeight());
            }
        });

        listeTypes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selection = listeTypes.getSelectedValue();
                typeMaintenancechamp.setText(selection);
                popupType.setVisible(false);
            }
        });

        panelNord.add(new JLabel("ID Maintenance :"));
        panelNord.add(idMaintenanceChamp);
        panelNord.add(new JLabel("Cout Maintenance :"));
        panelNord.add(coutMaintenancechamp);
        panelNord.add(new JLabel("Type Maintenance :"));
        panelNord.add(typeMaintenancechamp);
        panelNord.add(new JLabel("Date Maintenance :"));
        panelNord.add(dateMaintenanceChamp);
        panelNord.add(new JLabel("Filtrer par date (yyyy-MM-dd) :"));
        panelNord.add(filtreDateChamp);

       add(panelNord, BorderLayout.NORTH);

        String[] columns = {"IDMaintenance", "CoutMaintenance", "TypeMaintenance", "Date Maintenance"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelSud = new JPanel();
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");
        JButton boutonFiltrer = new JButton("Filtrer par date");
        JButton boutonReset = new JButton("Réinitialiser");

        panelSud.add(boutonAjouter);
        panelSud.add(boutonModifier);
        panelSud.add(boutonSupprimer);
        panelSud.add(boutonFiltrer);
        panelSud.add(boutonReset);
        add(panelSud, BorderLayout.SOUTH);

        boutonAjouter.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idMaintenanceChamp.getText().trim());
                int cout = Integer.parseInt(coutMaintenancechamp.getText().trim());
                String type = typeMaintenancechamp.getText().trim();
                LocalDate date = LocalDate.parse(dateMaintenanceChamp.getText().trim(), formattage);

                if (type.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Maintenance maintenance = new Maintenance();
                maintenance.setIdMaintenance(id);
                maintenance.setCoutMaintenance(cout);
                maintenance.setTypeMaintenance(type);
                maintenance.setDateMaintenance(date);

                maintenanceService.insertMaintenance(maintenance);
                chargerMaintenances();
                viderChamps();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer des nombres valides pour l'ID et le coût", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                idMaintenanceChamp.setText(model.getValueAt(i, 0).toString());
                coutMaintenancechamp.setText(model.getValueAt(i, 1).toString());
                typeMaintenancechamp.setText(model.getValueAt(i, 2).toString());
                dateMaintenanceChamp.setText(model.getValueAt(i, 3).toString());
            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    int id = Integer.parseInt(idMaintenanceChamp.getText().trim());
                    int cout = Integer.parseInt(coutMaintenancechamp.getText().trim());
                    String type = typeMaintenancechamp.getText().trim();
                    LocalDate date = LocalDate.parse(dateMaintenanceChamp.getText().trim(), formattage);

                    Maintenance maintenance = new Maintenance();
                    maintenance.setIdMaintenance(id);
                    maintenance.setCoutMaintenance(cout);
                    maintenance.setTypeMaintenance(type);
                    maintenance.setDateMaintenance(date);

                    maintenanceService.updateMaintenance(maintenance);
                    chargerMaintenances();
                    viderChamps();
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonSupprimer.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Maintenance maintenance = new Maintenance();
                    maintenance.setIdMaintenance(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    maintenance.setCoutMaintenance(Integer.parseInt(model.getValueAt(i, 1).toString()));
                    maintenance.setTypeMaintenance(model.getValueAt(i, 2).toString());
                    maintenance.setDateMaintenance(LocalDate.parse(model.getValueAt(i, 3).toString(), formattage));

                    maintenanceService.deleteMaintenance(maintenance);
                    chargerMaintenances();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonFiltrer.addActionListener(e -> {
            String dateStr = filtreDateChamp.getText().trim();
            if (!dateStr.isEmpty()) {
                try {
                    LocalDate dateFiltre = LocalDate.parse(dateStr, formattage);
                    model.setRowCount(0); // Vider tableau
                    Maintenances maintenances = maintenanceService.selectMaintenances();
                    if (maintenances != null && maintenances.getMaintenances() != null) {
                        maintenances.getMaintenances().stream()
                                .filter(m -> m.getDateMaintenance().equals(dateFiltre))
                                .forEach(m -> model.addRow(new Object[]{
                                        m.getIdMaintenance(),
                                        m.getCoutMaintenance(),
                                        m.getTypeMaintenance(),
                                        m.getDateMaintenance().toString()
                                }));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Date invalide. Format attendu : yyyy-MM-dd", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        boutonReset.addActionListener(e -> {
            try {
                chargerMaintenances();
                filtreDateChamp.setText("");
                viderChamps();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors du rechargement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerMaintenances();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des maintenances: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void chargerMaintenances() throws IOException, InterruptedException {
        model.setRowCount(0);
        Maintenances maintenances = maintenanceService.selectMaintenances();
        if (maintenances != null && maintenances.getMaintenances() != null) {
            maintenances.getMaintenances().stream()
                    .sorted((m1, m2) -> m1.getDateMaintenance().compareTo(m2.getDateMaintenance()))
                    .forEach(m -> model.addRow(new Object[]{
                            m.getIdMaintenance(),
                            m.getCoutMaintenance(),
                            m.getTypeMaintenance(),
                            m.getDateMaintenance().toString()
                    }));
        }
    }

    private void viderChamps() {
        idMaintenanceChamp.setText("");
        coutMaintenancechamp.setText("");
        typeMaintenancechamp.setText("");
        dateMaintenanceChamp.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MaintenanceFront::new);
    }
}
