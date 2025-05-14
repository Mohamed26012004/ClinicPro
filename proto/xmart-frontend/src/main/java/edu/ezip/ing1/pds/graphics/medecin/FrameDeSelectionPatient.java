package edu.ezip.ing1.pds.graphics.medecin;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.business.dto.Patient;
import edu.ezip.ing1.pds.business.dto.Patients;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.planning.ExamenService;
import edu.ezip.ing1.pds.services.planning.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class FrameDeSelectionPatient extends JFrame {


    final static String networkConfigFile = "network.yaml";
    static final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    static final PatientService patientService = new PatientService(networkConfig);

    private static DefaultTableModel modelPatient;
    private static JTable tablePatient;
    private static JPanel contentPane;

    public FrameDeSelectionPatient(){
        super("Sélectionner Patient");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = (JPanel) getContentPane();

        String[] columns = {"ID", "Nom", "Prénom", "Téléphone", "Adresse"};
        modelPatient = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

        };
        tablePatient = new JTable(modelPatient);
        tablePatient.setRowHeight(30);
        tablePatient.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(tablePatient);
        try {
            chargerPatients();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel pane = new JPanel(new FlowLayout());
        JButton selectionner = new JButton("Sélectionner");
        JButton enregistrer = new JButton("Enregistrer un patient");
        selectionner.setBackground(new Color(115, 91, 255));
        enregistrer.setBackground(new Color(72, 255, 0));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tablePatient.getSelectedRow();
                if (i > 0){
                    FrameInsertUpdatePlanification.modelPatient.setRowCount(0);
                    FrameInsertUpdatePlanification.modelPatient.addRow(new Object[]{
                            modelPatient.getValueAt(i, 0),
                            modelPatient.getValueAt(i, 1),
                            modelPatient.getValueAt(i, 2),
                            modelPatient.getValueAt(i, 3),
                            modelPatient.getValueAt(i, 4),
                    });
                    FrameInsertUpdatePlanification.tablePatient.setRowSelectionInterval(0, 0);
                }
                dispose();

            }
        });
        enregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameInsertPatient f = new FrameInsertPatient();
            }
        });
        pane.add(selectionner);
        pane.add(enregistrer);
        contentPane.add(pane, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void chargerPatients() throws IOException, InterruptedException {
        modelPatient.setRowCount(0);

        Patients patients = patientService.selectPatients();
        if (patients != null && patients.getPatients() != null) {
            ArrayList<Patient> list = new ArrayList<>(patients.getPatients());
            list.sort(Comparator.comparing(Patient::getNom));              //Order by nom
            for (Patient patient: list){
                modelPatient.addRow(new Object[]{
                        patient.getIdPatient(),
                        patient.getNom(),
                        patient.getPrenom(),
                        patient.getTelephone(),
                        patient.getAdresse()
                });
            }
        }
    }
}
