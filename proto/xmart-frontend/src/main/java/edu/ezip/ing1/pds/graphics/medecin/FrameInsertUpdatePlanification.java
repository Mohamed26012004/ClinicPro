package edu.ezip.ing1.pds.graphics.medecin;

import edu.ezip.ing1.pds.business.dto.Creneau;
import edu.ezip.ing1.pds.business.dto.PlanificationExamen;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.graphics.examen.FrameDeSelectionExamen;
import edu.ezip.ing1.pds.graphics.salle.FrameDeSelectionSalle;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FrameInsertUpdatePlanification extends JFrame {

    private PlanificationExamen dateEtCreneau;
    private static JPanel contentPane;
    public static DefaultTableModel modelPatient;
    public static JTable tablePatient;
    public static DefaultTableModel modelExamen;
    public static JTable tableExamen;
    public static DefaultTableModel modelSalle;
    public static JTable tableSalle;
    
    public FrameInsertUpdatePlanification(PlanificationExamen planificationExamen){

        super("Programmer un examen");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dateEtCreneau = planificationExamen;

        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(panelPatient());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10,30, 10));
        contentPane.add(panelExamen());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10,30, 10));
        contentPane.add(panelSalle());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10,30, 10));
        contentPane.add(southButton());

        setVisible(true);
    }
    
    public static JPanel panelPatient(){
        JPanel panel = new JPanel();
        Border titre = BorderFactory.createTitledBorder("PATIENT");
        Border espaceVide = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(BorderFactory.createCompoundBorder(titre, espaceVide));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton selectionner = new JButton("Sélectionner");
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameDeSelectionPatient f = new FrameDeSelectionPatient();
            }
        });
        selectionner.setBackground(new Color(115, 91, 255));
        panelBouton.add(selectionner);
        panel.add(panelBouton);

        String[] columns = {"ID", "Nom", "Prénom", "Téléphone", "Adresse"};
        modelPatient = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePatient = new JTable(modelPatient);
        tablePatient.setRowHeight(30);
        tablePatient.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(tablePatient);

        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    public static JPanel panelExamen(){
        JPanel panel = new JPanel();

        Border titre = BorderFactory.createTitledBorder("EXAMEN");
        Border espaceVide = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(BorderFactory.createCompoundBorder(titre, espaceVide));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton selectionner = new JButton("Sélectionner");
        selectionner.setBackground(new Color(115, 91, 255));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameDeSelectionExamen f = new FrameDeSelectionExamen();
            }
        });
        panelBouton.add(selectionner);
        panel.add(panelBouton);

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

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel panelSalle(){
        JPanel panel = new JPanel();

        Border titre = BorderFactory.createTitledBorder("SALLE");
        Border espaceVide = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(BorderFactory.createCompoundBorder(titre, espaceVide));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton selectionner = new JButton("Sélectionner");
        selectionner.setBackground(new Color(115, 91, 255));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameDeSelectionSalle f = new FrameDeSelectionSalle();
            }
        });
        panelBouton.add(selectionner);
        panel.add(panelBouton);

        String[] columns = {"ID", "Numero de Salle", "Type", "Statut"};
        modelSalle = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSalle = new JTable(modelSalle);
        tableSalle.setRowHeight(30);
        tableSalle.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(tableSalle);

        panel.add(scrollPane);

        return panel;
    }

    public JPanel southButton(){
        JPanel panel = new JPanel(new FlowLayout());
        JButton enregistrer = new JButton("Enregistrer");
        JButton annuler = new JButton("Annuler");

        enregistrer.setBackground(new Color(72, 255, 0));
        annuler.setBackground(new Color(255, 65, 65));

        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameInsertUpdatePlanification.this.dispose();
            }
        });

        panel.add(enregistrer);
        panel.add(annuler);
        return panel;
    }


    
}
