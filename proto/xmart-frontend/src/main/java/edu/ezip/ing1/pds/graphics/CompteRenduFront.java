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

import edu.ezip.ing1.pds.business.dto.CompteRendu;
import edu.ezip.ing1.pds.business.dto.CompteRendus;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesdpi.CompteRenduService;

public class CompteRenduFront extends JPanel {
    private JTextField id_compteRenduChamp, idPatientChamp, numeroADELIChamp, typeSymptomeChamp, descriptionSymptomeChamp;
    private DefaultTableModel model;
    private JTable table;
    private final CompteRenduService compteRenduService;


    public CompteRenduFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.compteRenduService = new CompteRenduService (networkConfig);
        setLayout(new BorderLayout());

//        Jthis this = new Jthis("Gestion des compte-rendus");
//        this.setSize(700, 400);
//        this.setDefaultCloseOperation(Jthis.DISPOSE_ON_CLOSE);
//        this.setLocationRelativeTo(null);

        JPanel panelNord = new JPanel(new GridLayout(2, 2, 5, 5));


        typeSymptomeChamp = new JTextField();
        descriptionSymptomeChamp= new JTextField();


        panelNord.add(new JLabel("Symptôme :"));
        panelNord.add(typeSymptomeChamp);
        panelNord.add(new JLabel("Description :"));
        panelNord.add(descriptionSymptomeChamp);


        add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID CompteRendu", "Id Patient", "Numéro ADELI", "Symptôme", "Description"};
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



                CompteRendu compteRendu = new CompteRendu();
                compteRendu.setTypeSymptome(typeSymptome);
                compteRendu.setDescriptionSymptome(descriptionSymptome);


                compteRenduService.insertCompteRendu(compteRendu);
                chargerCompteRendus();
                viderChamps();

            }catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                typeSymptomeChamp.setText(model.getValueAt(i, 1).toString());
                descriptionSymptomeChamp.setText(model.getValueAt(i, 2).toString());
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
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerCompteRendus();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des CompteRendus: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

//        this.setVisible(true);
    }

    private void chargerCompteRendus() throws IOException, InterruptedException {
        model.setRowCount(0);
        CompteRendus compteRendus = compteRenduService.selectCompteRendus();
        if (compteRendus != null && compteRendus.getCompteRendus() != null) {
            for (CompteRendu a : compteRendus.getCompteRendus()) {
                model.addRow(new Object[]{
                        a.getId_compteRendu(),
                        a.getIdPatient(),
                        a.getTypeSymptome(),
                        a.getDescriptionSymptome()
                });
            }
        }
    }

    private void viderChamps() {
        id_compteRenduChamp.setText("");
        idPatientChamp.setText("");
        numeroADELIChamp.setText("");
        typeSymptomeChamp.setText("");
        descriptionSymptomeChamp.setText("");

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CompteRenduFront::new);
    }
}
