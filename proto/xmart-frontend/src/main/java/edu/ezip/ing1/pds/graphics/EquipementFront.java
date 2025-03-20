package edu.ezip.ing1.pds.graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.business.dto.Equipement;
import edu.ezip.ing1.pds.business.dto.Equipements;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.EquipementService;

public class EquipementFront {
    private JTextField idEquipementChamp, coutEquipementchamp, nomEquipementchamp, dateEquipementChamp;
    private DefaultTableModel model;
    private JTable table;
    private final EquipementService equipementService;
    private DateTimeFormatter formattage = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public EquipementFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.equipementService = new EquipementService (networkConfig);

        JFrame frame = new JFrame("Gestion des Eqquipement");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panelNord = new JPanel(new GridLayout(4, 2, 5, 5));

        idEquipementChamp = new JTextField();
        nomEquipementchamp = new JTextField();
        coutEquipementchamp = new JTextField();
        dateEquipementChamp = new JTextField();

        panelNord.add(new JLabel("ID Equipement :"));
        panelNord.add(idEquipementChamp);
        panelNord.add(new JLabel("Cout :"));
        panelNord.add(coutEquipementchamp);
        panelNord.add(new JLabel("nom Equipement :"));
        panelNord.add(nomEquipementchamp);
        panelNord.add(new JLabel("Date Achat :"));
        panelNord.add(dateEquipementChamp);

        frame.add(panelNord, BorderLayout.NORTH);

        String[] columns = {"IDEquipement", "CoutEquipement", "NomEquipement", "Date Achat"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelSud = new JPanel();
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");

        panelSud.add(boutonAjouter);
        panelSud.add(boutonModifier);
        panelSud.add(boutonSupprimer);

        frame.add(panelSud, BorderLayout.SOUTH);

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
                JOptionPane.showMessageDialog(frame, "Le cout doit être un nombre valide",
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
                dateEquipementChamp.setText(model.getValueAt(i, 3).toString())
                ;
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

        try {
            chargerEquipements();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des equipements: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    private void chargerEquipements() throws IOException, InterruptedException {
        model.setRowCount(0);
        Equipements equipements = equipementService.selectEquipements();
        if (equipements != null && equipements.getEquipements() != null) {
            for (Equipement e : equipements.getEquipements()) {
                model.addRow(new Object[]{
                        e.getIdEquipement(),
                        e.getCoutEquipement(),
                        e.getNomEquipement(),
                        e.getDateEquipement()
                });
            }
        }
    }

    private void viderChamps() {
        idEquipementChamp.setText("");
        coutEquipementchamp.setText("");
        coutEquipementchamp.setText("");
        dateEquipementChamp.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EquipementFront::new);
    }
}
