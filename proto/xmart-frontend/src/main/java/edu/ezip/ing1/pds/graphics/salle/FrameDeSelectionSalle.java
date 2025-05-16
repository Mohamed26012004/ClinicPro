package edu.ezip.ing1.pds.graphics.salle;

import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.business.dto.Salles;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.medecin.FrameInsertUpdatePlanification;
import edu.ezip.ing1.pds.services.planning.ExamenService;
import edu.ezip.ing1.pds.services.planning.SalleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class FrameDeSelectionSalle extends JFrame {

    final static String networkConfigFile = "network.yaml";
    static final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    static final SalleService salleService = new SalleService(networkConfig);

    private static DefaultTableModel modelSalle;
    private static JTable tableSalle;
    private static JPanel contentPane;
    private static final String libre = "Libre";

    public FrameDeSelectionSalle(){
        super("Sélectionner Salle");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = (JPanel) getContentPane();

        String[] columns = {"ID", "Numéro de Salle", "Type", "Statut"};
        modelSalle = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

        };
        tableSalle = new JTable(modelSalle);
        tableSalle.setRowHeight(30);
        tableSalle.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(tableSalle);
        try {
            chargerSalles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel pane = new JPanel(new FlowLayout());
        JButton selectionner = new JButton("Sélectionner");
        selectionner.setFont(new Font("Arial", Font.BOLD, 16));
        selectionner.setBackground(new Color(115, 91, 255));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int i = tableSalle.getSelectedRow();
                if (i >= 0){
                    FrameInsertUpdatePlanification.modelSalle.setRowCount(0);
                    FrameInsertUpdatePlanification.modelSalle.addRow(new Object[]{
                            modelSalle.getValueAt(i, 0),
                            modelSalle.getValueAt(i, 1),
                            modelSalle.getValueAt(i, 2),
                            modelSalle.getValueAt(i, 3),
                    });
                    FrameInsertUpdatePlanification.tableSalle.setRowSelectionInterval(0, 0);
                }
                dispose();

            }
        });
        pane.add(selectionner);
        contentPane.add(pane, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void chargerSalles() throws IOException, InterruptedException {
        modelSalle.setRowCount(0);

        Salles salles = salleService.selectSalles();

        if (salles != null && salles.getSalles() != null) {
            ArrayList<Salle> list = new ArrayList<>(salles.getSalles());
            ArrayList<Salle> secondList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++){
                if(list.get(i).getStatut().equals(libre)){
                    secondList.add(list.get(i));
                }
            }
            secondList.sort(Comparator.comparing(Salle::getNumeroSalle));              //Order by nom
            for (Salle salle : secondList){
                modelSalle.addRow(new Object[]{
                        salle.getId(),
                        salle.getNumeroSalle(),
                        salle.getTypeSalle(),
                        salle.getStatut()
                });
            }
        }
    }
}
