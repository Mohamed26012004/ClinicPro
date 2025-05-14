package edu.ezip.ing1.pds.graphics.examen;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.medecin.FrameInsertUpdatePlanification;
import edu.ezip.ing1.pds.services.planning.ExamenService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class FrameDeSelectionExamen extends JFrame {

    final static String networkConfigFile = "network.yaml";
    static final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    static final ExamenService examenService = new ExamenService(networkConfig);

    private static DefaultTableModel modelExamen;
    private static JTable tableExamen;
    private static JPanel contentPane;

    public FrameDeSelectionExamen(){
        super("Sélectionner Examen");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = (JPanel) getContentPane();

        String[] columns = {"ID", "Nom", "Coût", "Durée"};
        modelExamen = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

        };
        tableExamen = new JTable(modelExamen);
        tableExamen.setRowHeight(30);
        tableExamen.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(tableExamen);
        try {
            chargerExamens();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel pane = new JPanel(new FlowLayout());
        JButton selectionner = new JButton("Sélectionner");
        selectionner.setBackground(new Color(115, 91, 255));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int i = tableExamen.getSelectedRow();
                if (i > 0){
                    FrameInsertUpdatePlanification.modelExamen.setRowCount(0);
                    FrameInsertUpdatePlanification.modelExamen.addRow(new Object[]{
                            modelExamen.getValueAt(i, 0),
                            modelExamen.getValueAt(i, 1),
                            modelExamen.getValueAt(i, 2),
                            modelExamen.getValueAt(i, 3),
                    });
                }
                dispose();

            }
        });
        pane.add(selectionner);
        contentPane.add(pane, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void chargerExamens() throws IOException, InterruptedException {
        modelExamen.setRowCount(0);

        Examens examens = examenService.selectExamens();
        if (examens != null && examens.getExamens() != null) {
            ArrayList<Examen> list = new ArrayList<>(examens.getExamens());
            list.sort(Comparator.comparing(Examen::getNom));              //Order by nom
            for (Examen examen : list){
                modelExamen.addRow(new Object[]{
                        examen.getId(),
                        examen.getNom(),
                        examen.getCout(),
                        examen.getDuree(),
                });
            }
        }
    }
}
