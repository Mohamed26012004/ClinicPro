package edu.ezip.ing1.pds.graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.business.dto.Diagnostic;
import edu.ezip.ing1.pds.business.dto.Diagnostics;
import edu.ezip.ing1.pds.business.dto.PlanificationExamen;
import edu.ezip.ing1.pds.business.dto.PlanificationExamens;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.planning.PlanificationService;
import edu.ezip.ing1.pds.servicesdpi.DiagnosticService;

public class DiagnosticFront extends JPanel {
    private JTextField codeCIM10Champ, nomMaladieChamp, descriptionDiagnosticChamp;
    private JComboBox<PlanificationExamen> planificationExamenComboBox;
    private DefaultTableModel model;
    private JTable table;
    private final DiagnosticService diagnosticService;
    private final PlanificationService planificationService;
    private ArrayList<PlanificationExamen> planificationExamensListe;

    public DiagnosticFront() throws IOException, InterruptedException {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.diagnosticService = new DiagnosticService(networkConfig);
        this.planificationService = new PlanificationService(networkConfig);
        setLayout(new BorderLayout());

        JPanel panelNord = new JPanel(new GridLayout(4, 2, 5, 5));

        planificationExamenComboBox = new JComboBox<>();
        planificationExamensListe = new ArrayList<>();
        PlanificationExamens planificationExamens = planificationService.selectPlanifications();

        if (planificationExamens != null && planificationExamens.getPlanifications() != null) {
            planificationExamensListe = new ArrayList<>(planificationExamens.getPlanifications());
            for (PlanificationExamen pe : planificationExamensListe) {
                planificationExamenComboBox.addItem(pe);
            }
        }

        codeCIM10Champ = new JTextField();
        nomMaladieChamp = new JTextField();
        descriptionDiagnosticChamp = new JTextField();

        panelNord.add(new JLabel("Code CIM10 :"));
        panelNord.add(codeCIM10Champ);
        panelNord.add(new JLabel("Maladie :"));
        panelNord.add(nomMaladieChamp);
        panelNord.add(new JLabel("Description :"));
        panelNord.add(descriptionDiagnosticChamp);
        panelNord.add(new JLabel("Planification :"));
        panelNord.add(planificationExamenComboBox);

        add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID Diagnostic", "ID Planification", "Code CIM10", "Maladie", "Description"};
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
                String codeCIM10 = codeCIM10Champ.getText().trim();
                String nomMaladie = nomMaladieChamp.getText().trim();
                String descriptionDiagnostic = descriptionDiagnosticChamp.getText().trim();
                PlanificationExamen selectedPlanification = (PlanificationExamen) planificationExamenComboBox.getSelectedItem();

                if (nomMaladie.isEmpty() || descriptionDiagnostic.isEmpty() || selectedPlanification == null ) {
                    JOptionPane.showMessageDialog(this, "Veuillez svp les champs nécessaires", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                Diagnostic diagnostic = new Diagnostic();
                diagnostic.setCodeCIM10(codeCIM10);
                diagnostic.setNomMaladie(nomMaladie);
                diagnostic.setDescription_Diagnostic(descriptionDiagnostic);
                diagnostic.setIdPlanification(selectedPlanification.getIdPlanification());

                diagnosticService.insertDiagnostic(diagnostic);
                chargerDiagnostics();
                viderChamps();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                codeCIM10Champ.setText(model.getValueAt(i, 2).toString());
                nomMaladieChamp.setText(model.getValueAt(i, 3).toString());
                descriptionDiagnosticChamp.setText(model.getValueAt(i, 4).toString());

                int idPlanification = Integer.parseInt(model.getValueAt(i, 1).toString());
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
                    Diagnostic diagnostic = new Diagnostic();
                    diagnostic.setId_Diagnostic(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    diagnostic.setCodeCIM10(codeCIM10Champ.getText().trim());
                    diagnostic.setNomMaladie(nomMaladieChamp.getText().trim());
                    diagnostic.setDescription_Diagnostic(descriptionDiagnosticChamp.getText().trim());

                    PlanificationExamen selectedPlanification = (PlanificationExamen) planificationExamenComboBox.getSelectedItem();
                    if (selectedPlanification != null) {
                        diagnostic.setIdPlanification(selectedPlanification.getIdPlanification());
                    }

                    diagnosticService.updateDiagnostic(diagnostic);
                    chargerDiagnostics();
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
                    Diagnostic diagnostic = new Diagnostic();
                    diagnostic.setId_Diagnostic(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    diagnosticService.deleteDiagnostic(diagnostic);
                    chargerDiagnostics();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerDiagnostics();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des diagnostics: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerDiagnostics() throws IOException, InterruptedException {
        model.setRowCount(0);
        Diagnostics diagnostics = diagnosticService.selectDiagnostics();
        if (diagnostics != null && diagnostics.getDiagnostics() != null) {
            for (Diagnostic d : diagnostics.getDiagnostics()) {
                model.addRow(new Object[]{
                        d.getId_Diagnostic(),
                        d.getIdPlanification(),
                        d.getCodeCIM10(),
                        d.getNomMaladie(),
                        d.getDescription_Diagnostic()
                });
            }
        }
    }

    private void viderChamps() {
        codeCIM10Champ.setText("");
        nomMaladieChamp.setText("");
        descriptionDiagnosticChamp.setText("");
        planificationExamenComboBox.setSelectedIndex(-1);
    }
}
