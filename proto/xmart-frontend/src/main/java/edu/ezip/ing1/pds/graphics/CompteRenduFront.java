package edu.ezip.ing1.pds.graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesdpi.CompteRenduService;
import edu.ezip.ing1.pds.services.planning.PlanificationService;
import edu.ezip.ing1.pds.business.dto.PlanificationExamen;

public class CompteRenduFront extends JPanel {
    private JTextField typeSymptomeChamp, descriptionSymptomeChamp;
    private JComboBox<PlanificationExamen> planificationExamenComboBox;
    private DefaultTableModel model;
    private JTable table;
    private final CompteRenduService compteRenduService;
    private final PlanificationService planificationService;
    private ArrayList<PlanificationExamen> planificationExamensListe;

    public CompteRenduFront() throws IOException, InterruptedException {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);

        this.compteRenduService = new CompteRenduService(networkConfig);
        this.planificationService = new PlanificationService(networkConfig);

        setLayout(new BorderLayout());

        JPanel panelNord = new JPanel(new GridLayout(3, 2, 5, 5));

        planificationExamenComboBox = new JComboBox<>();
        planificationExamensListe = new ArrayList<>();
        PlanificationExamens planificationExamens = planificationService.selectPlanifications();

        if (planificationExamens != null && planificationExamens.getPlanifications() != null) {
            planificationExamensListe = new ArrayList<>(planificationExamens.getPlanifications());
            for (PlanificationExamen pe : planificationExamensListe) {
                planificationExamenComboBox.addItem(pe);
            }
        }

        typeSymptomeChamp = new JTextField();
        descriptionSymptomeChamp = new JTextField();

        panelNord.add(new JLabel("Symptôme :"));
        panelNord.add(typeSymptomeChamp);
        panelNord.add(new JLabel("Description :"));
        panelNord.add(descriptionSymptomeChamp);
        panelNord.add(new JLabel("Planification :"));
        panelNord.add(planificationExamenComboBox);

        add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID ", "ID Planification", "Symptôme", "Description"};
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
                String typeSymptome = typeSymptomeChamp.getText().trim();
                String descriptionSymptome = descriptionSymptomeChamp.getText().trim();
                PlanificationExamen selectedPlanification = (PlanificationExamen) planificationExamenComboBox.getSelectedItem();

                if (typeSymptome.isEmpty() || descriptionSymptome.isEmpty() || selectedPlanification == null) {
                    JOptionPane.showMessageDialog(null, "Veuillez s'il vous plaît remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                CompteRendu compteRendu = new CompteRendu();
                compteRendu.setTypeSymptome(typeSymptome);
                compteRendu.setDescriptionSymptome(descriptionSymptome);
                compteRendu.setIdPlanification(selectedPlanification.getIdPlanification());

                compteRenduService.insertCompteRendu(compteRendu);
                chargerCompteRendus();
                viderChamps();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                int idPlanification = Integer.parseInt(model.getValueAt(i, 1).toString());
                typeSymptomeChamp.setText(model.getValueAt(i, 2).toString());
                descriptionSymptomeChamp.setText(model.getValueAt(i, 3).toString());

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
                    CompteRendu compteRendu = new CompteRendu();
                    compteRendu.setId_compteRendu(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    compteRendu.setTypeSymptome(typeSymptomeChamp.getText().trim());
                    compteRendu.setDescriptionSymptome(descriptionSymptomeChamp.getText().trim());

                    PlanificationExamen selectedPlanification = (PlanificationExamen) planificationExamenComboBox.getSelectedItem();
                    if (selectedPlanification != null) {
                        compteRendu.setIdPlanification(selectedPlanification.getIdPlanification());
                    }

                    compteRenduService.updateCompteRendu(compteRendu);
                    chargerCompteRendus();
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
                    CompteRendu compteRendu = new CompteRendu();
                    compteRendu.setId_compteRendu(Integer.parseInt(model.getValueAt(i, 0).toString()));

                    compteRenduService.deleteCompteRendu(compteRendu);
                    chargerCompteRendus();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerCompteRendus();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des CompteRendus: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerCompteRendus() throws IOException, InterruptedException {
        model.setRowCount(0);
        CompteRendus compteRendus = compteRenduService.selectCompteRendus();
        if (compteRendus != null && compteRendus.getCompteRendus() != null) {
            for (CompteRendu a : compteRendus.getCompteRendus()) {
                model.addRow(new Object[]{
                        a.getId_compteRendu(),
                        a.getIdPlanification(),
                        a.getTypeSymptome(),
                        a.getDescriptionSymptome()
                });
            }
        }
    }

    private void viderChamps() {
        typeSymptomeChamp.setText("");
        descriptionSymptomeChamp.setText("");
        planificationExamenComboBox.setSelectedIndex(-1);
    }
}
