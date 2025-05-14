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

import edu.ezip.ing1.pds.business.dto.AntecedentMedical;
import edu.ezip.ing1.pds.business.dto.AntecedentMedicals;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesdpi.AntecedentMedicalService;

public class AntecedentMedicalFront extends JPanel {
    private JTextField id_AntecedentMedicalChamp, idPatientChamp, type_antecedentMedicalChamp, description_antecedentMedicalChamp;
    private DefaultTableModel model;
    private JTable table;
    private final AntecedentMedicalService antecedentMedicalService;


    public AntecedentMedicalFront(){
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.antecedentMedicalService = new AntecedentMedicalService (networkConfig);
        setLayout(new BorderLayout());


//        Jthis this = new Jthis("Gestion des antécédents médicaux");
//        this.setSize(700, 400);
//        this.setDefaultCloseOperation(Jthis.DISPOSE_ON_CLOSE);
//        this.setLocationRelativeTo(null);

        JPanel panelNord = new JPanel(new GridLayout(2, 2, 5, 5));


        type_antecedentMedicalChamp = new JTextField();
        description_antecedentMedicalChamp= new JTextField();


        panelNord.add(new JLabel("Type d'antécédent médical :"));
        panelNord.add(type_antecedentMedicalChamp);
        panelNord.add(new JLabel("Description antécédent médical :"));
        panelNord.add(description_antecedentMedicalChamp);


        add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID AntecedentMedical", "Id Patient", "Type antécédent médical", "Description antécédent médical"};
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

                String typeAntecedentMedical = type_antecedentMedicalChamp.getText().trim();
                String description_AntecedentMedical = description_antecedentMedicalChamp.getText().trim();



                AntecedentMedical AntecedentMedical = new AntecedentMedical();
                AntecedentMedical.setType_antecedentMedical(typeAntecedentMedical);
                AntecedentMedical.setDescription_antecedentMedical(description_AntecedentMedical);


                antecedentMedicalService.insertAntecedentMedical(AntecedentMedical);
                chargerAntecedentMedicals();
                viderChamps();

            }catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                type_antecedentMedicalChamp.setText(model.getValueAt(i, 3).toString());
                description_antecedentMedicalChamp.setText(model.getValueAt(i, 4).toString());
            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    AntecedentMedical AntecedentMedical = new AntecedentMedical();
                    AntecedentMedical.setId_antecedentMedical(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    AntecedentMedical.setType_antecedentMedical(type_antecedentMedicalChamp.getText().trim());
                    AntecedentMedical.setDescription_antecedentMedical(description_antecedentMedicalChamp.getText().trim());

                    antecedentMedicalService.updateAntecedentMedical(AntecedentMedical);
                    chargerAntecedentMedicals();
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
                    AntecedentMedical AntecedentMedical = new AntecedentMedical();
                    AntecedentMedical.setId_antecedentMedical(Integer.parseInt(model.getValueAt(i, 0).toString()));


                    antecedentMedicalService.deleteAntecedentMedical(AntecedentMedical);
                    chargerAntecedentMedicals();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        try {
            chargerAntecedentMedicals();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des AntecedentMedicals: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

//        this.setVisible(true);
    }

    private void chargerAntecedentMedicals() throws IOException, InterruptedException {
        model.setRowCount(0);
        AntecedentMedicals antecedentMedicals = antecedentMedicalService.selectantecedentMedicals();
        if (antecedentMedicals != null && antecedentMedicals.getAntecedentMedicals() != null) {
            for (AntecedentMedical a : antecedentMedicals.getAntecedentMedicals()) {
                model.addRow(new Object[]{
                        a.getId_antecedentMedical(),
                        a.getIdPatient(),
                        a.getType_antecedentMedical(),
                        a.getDescription_antecedentMedical()
                });
            }
        }
    }

    private void viderChamps() {
        id_AntecedentMedicalChamp.setText("");
        idPatientChamp.setText("");
        type_antecedentMedicalChamp.setText("");
        description_antecedentMedicalChamp.setText("");

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AntecedentMedicalFront::new);
    }
}
