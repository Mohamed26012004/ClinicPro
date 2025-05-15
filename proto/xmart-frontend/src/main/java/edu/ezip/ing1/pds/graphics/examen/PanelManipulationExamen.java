package edu.ezip.ing1.pds.graphics.examen;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.planning.ExamenService;
import edu.ezip.ing1.pds.services.planning.PlanificationService;

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

public class PanelManipulationExamen extends JPanel {

    final static String networkConfigFile = "network.yaml";
    static final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    static final ExamenService examenService = new ExamenService(networkConfig);
    final PlanificationService planificationService = new PlanificationService(networkConfig);
    private final DateTimeFormatter formattage = DateTimeFormatter.ofPattern("HH:mm");

    private static DefaultTableModel model;
    private JTable table;
    private static JButton ajouter;
    private static JButton modifier;
    private static JButton supprimer;
    private static JScrollPane scrollPane;
    private static JPanel panneau = new JPanel(new BorderLayout());


    public PanelManipulationExamen() throws IOException, InterruptedException {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(this.toolBar(), BorderLayout.NORTH);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JLabel l = new JLabel("LISTE DES EXAMENS");
        l.setFont(new Font("Arial", Font.BOLD, 17));
        panel.add(l, BorderLayout.NORTH);

        String[] columns = {"ID", "Nom", "Coût (€)", "Durée"};
        model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        chargerExamens();
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
    }

    public static void chargerExamens() throws IOException, InterruptedException {
        model.setRowCount(0);

        Examens examens = examenService.selectExamens();
        if (examens != null && examens.getExamens() != null) {
            ArrayList<Examen> list = new ArrayList<>(examens.getExamens());
            list.sort(Comparator.comparing(Examen::getNom));              //Order by nom
            for (Examen examen : list){
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

        JButton addButton = new JButton("Nouvel Examen");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(new Color(151, 255, 110));
        JButton update = new JButton("Modifier");
        update.setFont(new Font("Arial", Font.PLAIN, 16));
        update.setBackground(new Color(127, 91, 255));
        JButton delete = new JButton("Supprimer");
        delete.setFont(new Font("Arial", Font.PLAIN, 16));
        delete.setBackground(new Color(255, 65, 65));



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

                        PlanificationExamen planificationExamen = new PlanificationExamen();
                        planificationExamen.setIdExamen(Integer.parseInt(model.getValueAt(i, 0).toString()));

                        PlanificationExamens plans = planificationService.selectIdPlanificationParExamen(planificationExamen);
                        if (!plans.getPlanifications().isEmpty()){
                            JOptionPane.showMessageDialog(null, "Examen PROGRAMME. Impossible de le supprimer." +
                                    "\nVeillez supprimer les rendez-vous et les planifications consernées par l'examen avant de le supprimer. ", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }else {
                            examenService.deleteExamen(examen);
                            chargerExamens();
                            JOptionPane.showMessageDialog(null, "Examen supprimé.", "Message", JOptionPane.INFORMATION_MESSAGE);

                        }
                    }
                }catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        update.addActionListener(new ActionListener() {
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

                        FrameCreationExamen f = new FrameCreationExamen(examen);
                        chargerExamens();
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FrameCreationExamen f = new FrameCreationExamen(null);
                    chargerExamens();
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        addButton.setBackground(new Color(113, 70, 255));
        update.setToolTipText("Modifier");
        delete.setToolTipText("Supprimer");
        bar.add(addButton);
        bar.addSeparator(new Dimension( 10, 10));
        bar.add(update);
        bar.addSeparator(new Dimension( 10, 10));
        bar.add(delete);
        return bar;
    }



}
