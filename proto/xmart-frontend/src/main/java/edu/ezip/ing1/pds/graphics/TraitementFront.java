package edu.ezip.ing1.pds.graphics;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.business.dto.PlanificationExamen;
import edu.ezip.ing1.pds.business.dto.PlanificationExamens;
import edu.ezip.ing1.pds.business.dto.Traitement;
import edu.ezip.ing1.pds.business.dto.Traitements;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.planning.PlanificationService;
import edu.ezip.ing1.pds.servicesdpi.TraitementService;

public class TraitementFront extends JPanel {
    private JTextField typeTraitementChamp, descriptionTraitementChamp, debutTraitementChamp, finTraitementChamp;
    private JComboBox<PlanificationExamen> planificationExamenComboBox;
    private DefaultTableModel model;
    private JTable table;
    private final TraitementService TraitementService;
    private final PlanificationService planificationService;
    private ArrayList<PlanificationExamen> planificationExamensListe;


    public TraitementFront() throws IOException, InterruptedException {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.TraitementService = new TraitementService (networkConfig);
        this.planificationService = new PlanificationService(networkConfig);
        setLayout(new BorderLayout());


        JPanel panelNord = new JPanel(new GridLayout(5, 5, 5, 5));

        planificationExamenComboBox = new JComboBox<>();
        planificationExamensListe = new ArrayList<>();
        PlanificationExamens planificationExamens = planificationService.selectPlanifications();


        if (planificationExamens != null && planificationExamens.getPlanifications() != null) {
            planificationExamensListe = new ArrayList<>(planificationExamens.getPlanifications());
            for (PlanificationExamen pe : planificationExamensListe) {
                planificationExamenComboBox.addItem(pe);
            }
        }


        typeTraitementChamp = new JTextField();
        descriptionTraitementChamp = new JTextField();
        debutTraitementChamp = new JTextField();
        finTraitementChamp = new JTextField();


        panelNord.add(new JLabel("Traitement :"));
        panelNord.add(typeTraitementChamp);
        panelNord.add(new JLabel("Description :"));
        panelNord.add(descriptionTraitementChamp);
        panelNord.add(new JLabel("Début :"));
        panelNord.add(debutTraitementChamp);
        panelNord.add(new JLabel("Fin :"));
        panelNord.add(finTraitementChamp);
        panelNord.add(new JLabel("Planification :"));
        panelNord.add(planificationExamenComboBox);


        add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID Traitement", "Id Planification", "Traitement", "Description", "Début", "Fin"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelSud = new JPanel();
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");

        panelSud.add(boutonAjouter);
        panelSud.add(boutonModifier);
        panelSud.add(boutonSupprimer);

        add(panelSud, BorderLayout.SOUTH);

        boutonAjouter.addActionListener(e -> {
            try {

                String typeTraitement = typeTraitementChamp.getText().trim();
                String descriptionTraitement = descriptionTraitementChamp.getText().trim();
                String debutTraitement = debutTraitementChamp.getText().trim();
                String finTraitement = finTraitementChamp.getText().trim();
                PlanificationExamen selectedPlanification = (PlanificationExamen) planificationExamenComboBox.getSelectedItem();


                if (selectedPlanification == null) {
                    JOptionPane.showMessageDialog(null, "Veuillez associer le traitement à un idPlanification svp", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Traitement traitement = new Traitement();
                traitement.setType_Traitement(typeTraitement);
                traitement.setDescription_Traitement(descriptionTraitement);
                traitement.setDebut_Traitement(debutTraitement);
                traitement.setFin_Traitement(finTraitement);
                traitement.setIdPlanification(selectedPlanification.getIdPlanification());

                TraitementService.insertTraitement(traitement);
                chargerTraitements();
                viderChamps();

            }catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                int idPlanification = Integer.parseInt(model.getValueAt(i, 1).toString());
                typeTraitementChamp.setText(model.getValueAt(i, 1).toString());
                descriptionTraitementChamp.setText(model.getValueAt(i, 2).toString());
                debutTraitementChamp.setText(model.getValueAt(i, 3).toString());
                finTraitementChamp.setText(model.getValueAt(i, 3).toString());


                for (int j = 0; j < planificationExamenComboBox.getItemCount(); j++) {
                    if (planificationExamenComboBox.getItemAt(j).getIdPlanification() == idPlanification) {
                        planificationExamenComboBox.setSelectedIndex(j);
                        break;
                    }
                }

            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Traitement Traitement = new Traitement();
                    Traitement.setId_Traitement(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    Traitement.setType_Traitement(typeTraitementChamp.getText().trim());
                    Traitement.setDebut_Traitement(debutTraitementChamp.getText().trim());
                    Traitement.setFin_Traitement(finTraitementChamp.getText().trim());

                    PlanificationExamen selectedPlanification = (PlanificationExamen) planificationExamenComboBox.getSelectedItem();
                    if (selectedPlanification != null) {
                        Traitement.setIdPlanification(selectedPlanification.getIdPlanification());
                    }

                    TraitementService.updateTraitement(Traitement);
                    chargerTraitements();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerTraitements();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des Traitements: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void chargerTraitements() throws IOException, InterruptedException {
        model.setRowCount(0);
        Traitements traitements = TraitementService.selectTraitements();
        if (traitements != null && traitements.getTraitements() != null) {
            for (Traitement a : traitements.getTraitements()) {
                model.addRow(new Object[]{
                        a.getId_Traitement(),
                        a.getIdPlanification(),
                        a.getType_Traitement(),
                        a.getDescription_Traitement(),
                        a.getDebut_Traitement(),
                        a.getFin_Traitement(),


                });
            }
        }
    }

    private void viderChamps() {
        typeTraitementChamp.setText("");
        descriptionTraitementChamp.setText("");
        debutTraitementChamp.setText("");
        finTraitementChamp.setText("");


    }


}
