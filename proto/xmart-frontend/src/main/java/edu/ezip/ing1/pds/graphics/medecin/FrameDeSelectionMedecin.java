package edu.ezip.ing1.pds.graphics.medecin;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.planning.MedecinService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class FrameDeSelectionMedecin extends JFrame{

    final static String networkConfigFile = "network.yaml";
    static final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    static final MedecinService medecinService = new MedecinService(networkConfig);

    private static DefaultTableModel modelMedecin;
    private static JTable tableMedecin;
    private static JPanel contentPane;

    public FrameDeSelectionMedecin(PlanificationExamen planificationExamen){
        super("Sélectionner un médecin");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = (JPanel) getContentPane();

        String[] columns = {"Numéro ADELI", "Nom", "Prénom", "Téléphone", "Spécialité", "Salaire"};
        modelMedecin = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMedecin = new JTable(modelMedecin);
        tableMedecin.setRowHeight(30);
        tableMedecin.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(tableMedecin);
        try {
            chargerMedecins(planificationExamen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel pane = new JPanel(new FlowLayout());
        JButton selectionner = new JButton("Sélectionner");
        selectionner.setFont(new Font("Arial", Font.PLAIN, 16));
        selectionner.setBackground(new Color(115, 91, 255));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tableMedecin.getSelectedRow();
                if (i >= 0){
                    FrameInsertUpdatePlanification.modelMedecin.setRowCount(0);
                    FrameInsertUpdatePlanification.modelMedecin.addRow(new Object[]{
                            modelMedecin.getValueAt(i, 0),
                            modelMedecin.getValueAt(i, 1),
                            modelMedecin.getValueAt(i, 2),
                            modelMedecin.getValueAt(i, 3),
                            modelMedecin.getValueAt(i, 4),
                            modelMedecin.getValueAt(i, 5)
                    });
                    FrameInsertUpdatePlanification.tableMedecin.setRowSelectionInterval(0, 0);
                }
                dispose();

            }
        });
        pane.add(selectionner);
        contentPane.add(pane, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void chargerMedecins(PlanificationExamen planificationExamen) throws IOException, InterruptedException {
        modelMedecin.setRowCount(0);

        Medecins medecins = medecinService.selectMedecinDisponibleByDateAndCreneau(planificationExamen);
        if (medecins != null && medecins.getMedecins() != null) {
            ArrayList<Medecin> list = new ArrayList<>(medecins.getMedecins());
            list.sort(Comparator.comparing(Medecin::getNom));              //Order by nom
            for (Medecin medecin: list){
                modelMedecin.addRow(new Object[]{
                        medecin.getNumeroADELI(),
                        medecin.getNom(),
                        medecin.getPrenom(),
                        medecin.getTelephone(),
                        medecin.getSpecialite(),
                        medecin.getSalaire()
                });
            }
        }
    }
}
