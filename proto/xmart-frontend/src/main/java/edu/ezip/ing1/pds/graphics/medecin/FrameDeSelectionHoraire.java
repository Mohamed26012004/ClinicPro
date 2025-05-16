package edu.ezip.ing1.pds.graphics.medecin;

import edu.ezip.ing1.pds.business.dto.Horaire;
import edu.ezip.ing1.pds.business.dto.Horaires;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.planning.HoraireService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class FrameDeSelectionHoraire extends JFrame {


    final static String networkConfigFile = "network.yaml";
    static final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    static final HoraireService horaireService = new HoraireService(networkConfig);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private static DefaultTableModel modelHoraire;
    private static JTable tableHorairee;
    private static JPanel contentPane;

    private static ArrayList<Horaire> listHoraire;

    public FrameDeSelectionHoraire(ArrayList<Horaire> listHoraire){
        super("Sélectionner Patient");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        listHoraire.clear();
        contentPane = (JPanel) getContentPane();

        String[] columns = {"ID", "Jour", "Heure de début", "Heure de fin"};
        modelHoraire = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

        };
        tableHorairee = new JTable(modelHoraire);
        tableHorairee.setRowHeight(30);
        tableHorairee.setFont(new Font("Arial", Font.PLAIN, 15));
        tableHorairee.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableHorairee);
        try {

            chargerPatients();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void chargerPatients() throws IOException, InterruptedException {
        modelHoraire.setRowCount(0);

        ArrayList<Horaire> horaires = listHoraire;
        if (horaires != null ) {
            ArrayList<Horaire> list = new ArrayList<>( horaires);
            list.sort(Comparator.comparing(Horaire :: getJour).thenComparing(Horaire :: getHeureDebut));              //Order by nom
            for (Horaire horaire: list){
                modelHoraire.addRow(new Object[]{
                        horaire.getId(),
                        horaire.getJour(),
                        horaire.getHeureDebut(),
                        horaire.getHeureFin()
                });
            }
        }
    }

    public static ArrayList<Horaire> getListHoraire() {
        return listHoraire;
    }
}
