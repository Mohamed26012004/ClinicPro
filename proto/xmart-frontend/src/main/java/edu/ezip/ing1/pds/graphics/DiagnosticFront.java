package edu.ezip.ing1.pds.graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.business.dto.Diagnostic;
import edu.ezip.ing1.pds.business.dto.Diagnostics;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesdpi.DiagnosticService;

public class DiagnosticFront extends JPanel{
    private JTextField id_DiagnosticChamp, id_PatientChamp, numeroADELIChamp, codeCIM10Champ, nomMaladieChamp, descriptionDiagnosticChamp;
    private DefaultTableModel model;
    private JTable table;
    private final DiagnosticService diagnosticService;


    public DiagnosticFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.diagnosticService = new DiagnosticService (networkConfig);
        setLayout(new BorderLayout());

//        Jthis this = new Jthis("Gestion des diagnostics");
//        this.setSize(700, 400);
//        this.setDefaultCloseOperation(Jthis.DISPOSE_ON_CLOSE);
//        this.setLocationRelativeTo(null);

        JPanel panelNord = new JPanel(new GridLayout(4, 2, 5, 5));


        codeCIM10Champ = new JTextField();
        nomMaladieChamp= new JTextField();
        descriptionDiagnosticChamp = new JTextField();


        panelNord.add(new JLabel("Code CIM10 :"));
        panelNord.add(codeCIM10Champ);
        panelNord.add(new JLabel("Nom maladie :"));
        panelNord.add(nomMaladieChamp);
        panelNord.add(new JLabel("Description diagnostic :"));
        panelNord.add(descriptionDiagnosticChamp);

        add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID_Diagnostic", "ID Patient", "Numéro ADELI", "Code CIM10", "Nom maladie", "Description diagnostic"};
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



                if (descriptionDiagnostic.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez svp insérer une description du diagnostic",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Diagnostic diagnostic = new Diagnostic();
                diagnostic.setCodeCIM10(codeCIM10);
                diagnostic.setNomMaladie(nomMaladie);
                diagnostic.setDescription_Diagnostic(descriptionDiagnostic);

                diagnosticService.insertDiagnostic(diagnostic);
                chargerDiagnostics();
                viderChamps();

            }catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                codeCIM10Champ.setText(model.getValueAt(i, 1).toString());
                nomMaladieChamp.setText(model.getValueAt(i, 2).toString());
                descriptionDiagnosticChamp.setText(model.getValueAt(i, 3).toString())
                ;
            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Diagnostic Diagnostic = new Diagnostic();
                    Diagnostic.setId_Diagnostic(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    Diagnostic.setCodeCIM10(codeCIM10Champ.getText().trim());
                    Diagnostic.setNomMaladie(nomMaladieChamp.getText().trim());
                    Diagnostic.setDescription_Diagnostic(descriptionDiagnosticChamp.getText().trim());

                    diagnosticService.updateDiagnostic(Diagnostic);
                    chargerDiagnostics();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonSupprimer.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Diagnostic Diagnostic = new Diagnostic();
                    Diagnostic.setId_Diagnostic(Integer.parseInt(model.getValueAt(i, 0).toString()));


                    diagnosticService.deleteDiagnostic(Diagnostic);
                    chargerDiagnostics();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerDiagnostics();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des diagnostics: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

//        this.setVisible(true);
    }

    private void chargerDiagnostics() throws IOException, InterruptedException {
        model.setRowCount(0);
        Diagnostics diagnostics = diagnosticService.selectDiagnostics();
        if (diagnostics != null && diagnostics.getDiagnostics() != null) {
            for (Diagnostic d : diagnostics.getDiagnostics()) {
                model.addRow(new Object[]{
                        d.getId_Diagnostic(),
                        d.getCodeCIM10(),
                        d.getNomMaladie(),
                        d.getDescription_Diagnostic()
                });
            }
        }
    }

    private void viderChamps() {
        id_DiagnosticChamp.setText("");
        id_PatientChamp.setText("");
        numeroADELIChamp.setText("");
        codeCIM10Champ.setText("");
        nomMaladieChamp.setText("");
        descriptionDiagnosticChamp.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DiagnosticFront::new);
    }
}
