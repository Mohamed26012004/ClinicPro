package edu.ezip.ing1.pds.graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;


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

import edu.ezip.ing1.pds.business.dto.Traitement;
import edu.ezip.ing1.pds.business.dto.Traitements;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesdpi.TraitementService;

public class TraitementFront {
    private JTextField id_TraitementChamp, idPatientChamp, numeroADELIChamp, typeTraitementChamp, descriptionTraitementChamp, debutTraitementChamp, finTraitementChamp;
    private DefaultTableModel model;
    private JTable table;
    private final TraitementService TraitementService;


    public TraitementFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.TraitementService = new TraitementService (networkConfig);

        JFrame frame = new JFrame("Gestion des traitements");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panelNord = new JPanel(new GridLayout(2, 2, 5, 5));


        typeTraitementChamp = new JTextField();
        descriptionTraitementChamp = new JTextField();
        debutTraitementChamp = new JTextField();
        finTraitementChamp = new JTextField();


        panelNord.add(new JLabel("Type de traitement:"));
        panelNord.add(typeTraitementChamp);
        panelNord.add(new JLabel("Description :"));
        panelNord.add(descriptionTraitementChamp);
        panelNord.add(new JLabel("Début:"));
        panelNord.add(debutTraitementChamp);
        panelNord.add(new JLabel("Fin:"));
        panelNord.add(finTraitementChamp);


        frame.add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID Traitement", "Id Patient", "Numéro ADELI", "Type de traitement", "Description", "Début", "Fin"};
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

                String typeTraitement = typeTraitementChamp.getText().trim();
                String descriptionTraitement = descriptionTraitementChamp.getText().trim();
                String debutTraitement = debutTraitementChamp.getText().trim();
                String finTraitement = finTraitementChamp.getText().trim();
                Traitement traitement = new Traitement();
                traitement.setType_Traitement(typeTraitement);
                traitement.setDescription_Traitement(descriptionTraitement);


                TraitementService.insertTraitement(traitement);
                chargerTraitements();
                viderChamps();

            }catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                typeTraitementChamp.setText(model.getValueAt(i, 1).toString());
                descriptionTraitementChamp.setText(model.getValueAt(i, 2).toString());
                debutTraitementChamp.setText(model.getValueAt(i, 3).toString());
                finTraitementChamp.setText(model.getValueAt(i, 3).toString());

            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Traitement Traitement = new Traitement();
                    Traitement.setId_Traitement(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    Traitement.setIdPatient(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    Traitement.setNumeroADELI(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    Traitement.setType_Traitement(typeTraitementChamp.getText().trim());
                    Traitement.setDebut_Traitement(debutTraitementChamp.getText().trim());
                    Traitement.setFin_Traitement(finTraitementChamp.getText().trim());


                    TraitementService.updateTraitement(Traitement);
                    chargerTraitements();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonSupprimer.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Traitement traitement = new Traitement();
                    traitement.setId_Traitement(Integer.parseInt(model.getValueAt(i, 0).toString()));


                    TraitementService.deleteTraitement(traitement);
                    chargerTraitements();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerTraitements();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des Traitements: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    private void chargerTraitements() throws IOException, InterruptedException {
        model.setRowCount(0);
        Traitements traitements = TraitementService.selectTraitements();
        if (traitements != null && traitements.getTraitements() != null) {
            for (Traitement a : traitements.getTraitements()) {
                model.addRow(new Object[]{
                        a.getId_Traitement(),
                        a.getIdPatient(),
                        a.getNumeroADELI(),
                        a.getType_Traitement(),
                        a.getDescription_Traitement(),
                        a.getDebut_Traitement(),
                        a.getFin_Traitement(),


                });
            }
        }
    }

    private void viderChamps() {
        id_TraitementChamp.setText("");
        idPatientChamp.setText("");
        numeroADELIChamp.setText("");
        typeTraitementChamp.setText("");
        descriptionTraitementChamp.setText("");
        debutTraitementChamp.setText("");
        finTraitementChamp.setText("");


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TraitementFront::new);
    }
}
