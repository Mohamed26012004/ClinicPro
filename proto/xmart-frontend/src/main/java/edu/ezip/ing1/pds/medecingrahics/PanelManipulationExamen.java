package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.business.dto.Paiement;
import edu.ezip.ing1.pds.business.dto.Paiements;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ExamenService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PanelManipulationExamen extends JPanel {

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final ExamenService examenService = new ExamenService(networkConfig);
    private final DateTimeFormatter formattage = DateTimeFormatter.ofPattern("HH:mm");

    private InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("delete_button.png");
    private String deleteFileNameButton = "/delete_button.png";
    private String addFileNameButton = "add_button.png";
    private String updateFileNameButton = "update_button.png";
    private String informationFileNameButton = "information_button.png";

    private DefaultTableModel model;
    private JTable table;
    private static JButton ajouter;
    private static JButton modifier;
    private static JButton supprimer;
    private static JScrollPane scrollPane;
    private static JPanel panneau = new JPanel(new BorderLayout());


    public PanelManipulationExamen() throws IOException, InterruptedException {

        setLayout(new BorderLayout());


        add(this.toolBar(), BorderLayout.NORTH);
        String[] columns = {"ID", "Nom", "Coût", "Durée"};
        model = new DefaultTableModel(columns, 0);
        chargerExamens();
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }


//    public static JPanel afficheExamens() {
//        PanelManipulationExamen panel = new  PanelManipulationExamen();
//        scrollPane = new JScrollPane(panel);
//
//        panneau.removeAll();
//        panneau.add(scrollPane, BorderLayout.CENTER);
//
//        panneau.add(boutons(), BorderLayout.NORTH);
//
//        panneau.revalidate();
//        panneau.repaint();
//
//        return panneau;
//    }

//    public static JPanel boutons(){
//        JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        ajouter = new JButton("Ajouter");
//        ajouter.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                FrameCreationExamen fen = new FrameCreationExamen(null);
//                PanelManipulationExamen.afficheExamens().revalidate();
//                PanelManipulationExamen.afficheExamens().repaint();
//            }
//        });
//        pane.add(ajouter);
//
//        modifier = new JButton("Modifier");
//        modifier.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Examen examen = new Examen();
//                PanelExamen p = PanelExamen.panelExamenCliquer;
//                p.setBackground(null);
//                examen = p.ExamenOfPanel();
//                FrameCreationExamen fen = new FrameCreationExamen(examen);
//                PanelManipulationExamen.afficheExamens().revalidate();
//                PanelManipulationExamen.afficheExamens().repaint();
//            }
//        });
//        pane.add(modifier);
//        supprimer = new JButton("Supprimer");
//        supprimer.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Examen examen = new Examen();
//                PanelExamen p = PanelExamen.panelExamenCliquer;
//                p.setBackground(null);
//                examen = p.ExamenOfPanel();
//
//                final String networkConfigFile = "network.yaml";
//                final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
//                final ExamenService examenService = new ExamenService(networkConfig);
//                try {
//                    examenService.deleteExamen(examen);
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//                PanelManipulationExamen.afficheExamens().revalidate();
//                PanelManipulationExamen.afficheExamens().repaint();
//            }
//        });
//        pane.add(supprimer);
//
//        return pane;
//    }


    public void chargerExamens() throws IOException, InterruptedException {
        model.setRowCount(0);

        Examens examens = examenService.selectExamens();
        if (examens != null && examens.getExamens() != null) {
            for (Examen examen : examens.getExamens()){
                model.addRow(new Object[]{
                        examen.getId(),
                        examen.getNom(),
                        examen.getCout(),
                        examen.getDuree(),
                });
            }
        }
    }

    public JToolBar toolBar(){
        JToolBar bar = new JToolBar();


        JLabel label = new JLabel("LISTE DES EXAMENS");

        JButton addButton = new JButton(new ImageIcon(addFileNameButton));
        JButton update = new JButton(new ImageIcon(updateFileNameButton));
        JButton delete = new JButton(new ImageIcon(deleteFileNameButton));


        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int i = table.getSelectedRow();
                    if (i >= 0) {
                        Examen examen = new Examen();
                        examen.setId(Integer.parseInt(model.getValueAt(i, 0).toString()));
                        examen.setNom(model.getValueAt(i, 1).toString());
                        examen.setCout(Double.parseDouble(model.getValueAt(i,2).toString()));
                        examen.setDuree(LocalTime.parse(model.getValueAt(i, 3).toString(), formattage));

                        examenService.deleteExamen(examen);
                        chargerExamens();
                    }

                }catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        bar.add(label);
        bar.add(addButton);
        bar.add(update);
        bar.add(delete);
        return bar;
    }
}
