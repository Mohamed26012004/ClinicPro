package edu.ezip.ing1.pds.graphics;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import edu.ezip.ing1.pds.business.dto.Equipement;
import edu.ezip.ing1.pds.business.dto.Equipements;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.EquipementService;

public class EquipementFront extends JPanel{
    private JTextField idEquipementChamp, coutEquipementchamp, nomEquipementchamp, dateEquipementChamp, filtreDateChamp;
    private DefaultTableModel model;
    private JTable table;
    private final EquipementService equipementService;
    private final DateTimeFormatter formattage = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public EquipementFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.equipementService = new EquipementService(networkConfig);

        JFrame frame = new JFrame("Gestion des Équipements");
        setSize(700, 400);

        setLayout(new BorderLayout());
        JPanel panelNord = new JPanel(new GridLayout(5, 2, 5, 5));

        idEquipementChamp = new JTextField();
        coutEquipementchamp = new JTextField();
        nomEquipementchamp = new JTextField();
        nomEquipementchamp.setEditable(false); // Empêche la saisie manuelle
        dateEquipementChamp = new JTextField();
        filtreDateChamp = new JTextField();


        String[] nomsEquipementPredefinis = {
                "Stéthoscope", "Tensiomètre", "Thermomètre", "Lit", "Gants", "Seringue", "Autoclave"
        };
        JList<String> listeEquipements = new JList<>(nomsEquipementPredefinis);
        JScrollPane scrollPane = new JScrollPane(listeEquipements);
        scrollPane.setPreferredSize(new Dimension(150, 100));
        JPopupMenu popupNomEquipement = new JPopupMenu();
        popupNomEquipement.setLayout(new BorderLayout());
        popupNomEquipement.add(scrollPane, BorderLayout.CENTER);

        nomEquipementchamp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                popupNomEquipement.show(nomEquipementchamp, 0, nomEquipementchamp.getHeight());
            }
        });

        listeEquipements.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selection = listeEquipements.getSelectedValue();
                nomEquipementchamp.setText(selection);
                popupNomEquipement.setVisible(false);
            }
        });

        panelNord.add(new JLabel("ID Équipement :"));
        panelNord.add(idEquipementChamp);
        panelNord.add(new JLabel("Coût :"));
        panelNord.add(coutEquipementchamp);
        panelNord.add(new JLabel("Nom Équipement :"));
        panelNord.add(nomEquipementchamp);
        panelNord.add(new JLabel("Date Achat :"));
        panelNord.add(dateEquipementChamp);
        panelNord.add(new JLabel("Filtrer par date (yyyy-MM-dd) :"));
        panelNord.add(filtreDateChamp);

        add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID Équipement", "Coût", "Nom", "Date Achat"};
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
                int id = Integer.parseInt(idEquipementChamp.getText().trim());
                int cout = Integer.parseInt(coutEquipementchamp.getText().trim());
                String nomEquipement = nomEquipementchamp.getText().trim();
                LocalDate dateAchat = LocalDate.parse(dateEquipementChamp.getText().trim(), formattage);

                if (nomEquipement.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Tous les champs doivent être remplis",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Equipement equipement = new Equipement();
                equipement.setIdEquipement(id);
                equipement.setCoutEquipement(cout);
                equipement.setNomEquipement(nomEquipement);
                equipement.setDateEquipement(dateAchat);

                equipementService.insertEquipement(equipement);
                chargerEquipements();
                viderChamps();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Veuillez entrer des nombres valides pour l'ID et le coût",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                idEquipementChamp.setText(model.getValueAt(i, 0).toString());
                coutEquipementchamp.setText(model.getValueAt(i, 1).toString());
                nomEquipementchamp.setText(model.getValueAt(i, 2).toString());
                dateEquipementChamp.setText(model.getValueAt(i, 3).toString());
            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Equipement equipement = new Equipement();
                    equipement.setIdEquipement(Integer.parseInt(idEquipementChamp.getText().trim()));
                    equipement.setCoutEquipement(Integer.parseInt(coutEquipementchamp.getText().trim()));
                    equipement.setNomEquipement(nomEquipementchamp.getText().trim());
                    equipement.setDateEquipement(LocalDate.parse(dateEquipementChamp.getText().trim(), formattage));

                    equipementService.updateEquipement(equipement);
                    chargerEquipements();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la modification: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonSupprimer.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Equipement equipement = new Equipement();
                    equipement.setIdEquipement(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    equipement.setCoutEquipement(Integer.parseInt(model.getValueAt(i, 1).toString()));
                    equipement.setNomEquipement(model.getValueAt(i, 2).toString());
                    equipement.setDateEquipement(LocalDate.parse(model.getValueAt(i, 3).toString(), formattage));

                    equipementService.deleteEquipement(equipement);
                    chargerEquipements();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonFiltrer.addActionListener(e -> {
            String dateStr = filtreDateChamp.getText().trim();
            if (!dateStr.isEmpty()) {
                try {
                    LocalDate dateFiltre = LocalDate.parse(dateStr, formattage);
                    model.setRowCount(0); // Vider tableau
                    Equipements equipements = equipementService.selectEquipements();
                    if (equipements != null && equipements.getEquipements() != null) {
                        equipements.getEquipements().stream()
                                .filter(c -> c.getDateEquipement().equals(dateFiltre))
                                .forEach(c -> model.addRow(new Object[]{
                                        c.getIdEquipement(),
                                        c.getCoutEquipement(),
                                        c.getNomEquipement(),
                                        c.getDateEquipement().toString()
                                }));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Date invalide. Format attendu : yyyy-MM-dd", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        boutonReset.addActionListener(e -> {
            try {
                chargerEquipements();
                filtreDateChamp.setText("");
                viderChamps();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors du rechargement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerEquipements();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des équipements: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void chargerEquipements() throws IOException, InterruptedException {
        model.setRowCount(0);


        Equipements equipements = equipementService.selectEquipements();


        if (equipements != null && equipements.getEquipements() != null) {
            equipements.getEquipements().stream()
                    .sorted((e1, e2) -> e1.getDateEquipement().compareTo(e2.getDateEquipement()))
                    .forEach(e -> model.addRow(new Object[]{
                            e.getIdEquipement(),
                            e.getCoutEquipement(),
                            e.getNomEquipement(),
                            e.getDateEquipement()
                    }));
        }
    }

    private void viderChamps() {
        idEquipementChamp.setText("");
        coutEquipementchamp.setText("");
        nomEquipementchamp.setText("");
        dateEquipementChamp.setText("");
        filtreDateChamp.setText("");
    }

}
