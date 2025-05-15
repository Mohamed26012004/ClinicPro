package edu.ezip.ing1.pds.graphics.rendezvous;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.graphics.medecin.FrameInsertUpdatePlanification;
import edu.ezip.ing1.pds.services.planning.CreneauService;
import edu.ezip.ing1.pds.services.planning.MedecinService;
import edu.ezip.ing1.pds.services.planning.PlanificationService;
import edu.ezip.ing1.pds.services.planning.PlanificationWithNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class PanelRendezVous extends JPanel {

    private final static String LoggingLabel = "FrontEnd - HoraireService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final static MedecinService medecinService = new MedecinService(networkConfig);
    final static PlanificationWithNameService planificationWithNameService = new PlanificationWithNameService(networkConfig);
    final static CreneauService creneauService = new CreneauService(networkConfig);
    final static PlanificationService planificationService = new PlanificationService(networkConfig);

    public static PlanificationExamen planificationDuMedecin = new PlanificationExamen();
    public static DefaultTableModel modelPlanification;
    public static JTable tablePlanification;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    public static JRadioButton planningButton = new JRadioButton("Planning", true);
    public static JRadioButton disponibiliteButton = new JRadioButton("Disponibilité");
    public static DefaultTableModel modelDisponibilite;
    public static JTable tableDisponibilite;

    private static CardLayout rightCardLayout = new CardLayout();
    private static JPanel rightPanel = new JPanel(rightCardLayout);

    public PanelRendezVous() throws IOException, InterruptedException {

        setLayout(new BorderLayout());
        add(leftPanel(), BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        rightPanel.add(rightPanelPlanning(), "Planning");
        rightPanel.add(rightPanelDisponibilite(), "Disponibilite");
        if(planningButton.isSelected()){
            rightCardLayout.show(rightPanel, "Planning");
        }else if (disponibiliteButton.isSelected()){
            rightCardLayout.show(rightPanel, "Disponibilite");
        }

    }


    public static JPanel leftPanel() throws IOException, InterruptedException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(null);
        JPanel panel1 = new JPanel();

        // Planning ou disponiblité
        planningButton = new JRadioButton("Planning", true);
        planningButton.setFont(new Font("Arial", Font.PLAIN, 16));
        disponibiliteButton = new JRadioButton("Disponibilité");
        disponibiliteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        ButtonGroup group = new ButtonGroup();
        group.add(planningButton);
        group.add(disponibiliteButton);

        planningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightCardLayout.show(rightPanel, "Planning");
            }
        });
        disponibiliteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightCardLayout.show(rightPanel, "Disponibilite");
            }
        });

        panel1.add(planningButton);
        panel1.add(disponibiliteButton);
        panel.add(panel1);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 40, 10));

        JLabel label = Fenetre.createLabel("Choisir une date");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(Fenetre.createLabel("Choisir une date"));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel2 = new JPanel(new BorderLayout());

        DatePickerSettings settings = new DatePickerSettings();
        settings.setLocale(java.util.Locale.FRANCE);
        settings.setAllowKeyboardEditing(false);      //Empêche l'utilisateur de saisir une date et l'oblige à choisir.

        //calendrier
        DatePicker calandrier = new DatePicker(settings);
        calandrier.setDateToToday();
        calandrier.addDateChangeListener(event -> {
            LocalDate chooseDate = event.getNewDate();
            if (chooseDate != null) {
                planificationDuMedecin.setDatePlanification(chooseDate);
            }
        });

        panel2.add(calandrier);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(panel2);

        JPanel panel3 = new JPanel(new FlowLayout());
        JButton valider = new JButton("Valider");
        valider.setFont(new Font("Arial", Font.PLAIN, 16));
        valider.setBackground(new Color(165, 172, 250));
        valider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                planificationDuMedecin.setDatePlanification(calandrier.getDate());
                try {
                    if(planningButton.isSelected()){
                        chargerPlanification(planificationDuMedecin);
                    }else if (disponibiliteButton.isSelected()){
                        chargerDisponibilite(planificationDuMedecin);
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        panel3.add(valider);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(panel3);
        return panel;
    }

    public JPanel rightPanelPlanning(){

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columns = {"ID","Heure de début", "Heure de Fin", "Nom Médecin", "Nom Patient", "Nom Examen", "Numéro de Salle"};
        modelPlanification = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePlanification = new JTable(modelPlanification);
        tablePlanification.setRowHeight(30);
        tablePlanification.setFont(new Font("Arial", Font.PLAIN, 15));
        tablePlanification.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tablePlanification);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(this.planningToolBar(), BorderLayout.NORTH );

        return panel;
    }

    public static void chargerPlanification(PlanificationExamen planificationExamen) throws IOException, InterruptedException {
        modelPlanification.setRowCount(0);

        PlanificationWithNames planificationWithNames = planificationWithNameService.selectPlanificationWithNames(planificationExamen);

        if (planificationWithNames != null && planificationWithNames.getPlanificationWithNames() != null) {
            ArrayList<PlanificationWithName> list = new ArrayList<>(planificationWithNames.getPlanificationWithNames());
            list.sort(Comparator.comparing(PlanificationWithName::getHeureDebut)
                    .thenComparing(PlanificationWithName :: getNomMedecin));              //Order by nom
            if (!list.isEmpty()) {
                for (PlanificationWithName plan : list) {
                    modelPlanification.addRow(new Object[]{
                            plan.getIdPlanification(),
                            plan.getHeureDebut(),
                            plan.getHeureFin(),
                            plan.getNomMedecin() + "  " + plan.getPrenomMedecin(),
                            plan.getNomPatient() + "  " + plan.getPrenomPatient(),
                            plan.getNomExamen(),
                            plan.getNumeroSalle()
                    });
                }
            }else{
                JOptionPane.showMessageDialog(null, "Aucun examen programmé.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public JPanel rightPanelDisponibilite(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        String[] columns = {"Heure de début", "Heure de Fin"};
        modelDisponibilite = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableDisponibilite = new JTable(modelDisponibilite);
        tableDisponibilite.setRowHeight(30);
        tableDisponibilite.setFont(new Font("Arial", Font.PLAIN, 15));
        tableDisponibilite.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tableDisponibilite);
        scrollPane.setPreferredSize(new Dimension(500, 600));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(disponibiliteToolBar(), BorderLayout.NORTH );

        return panel;
    }

    public static void chargerDisponibilite(PlanificationExamen planificationExamen) throws IOException, InterruptedException {

        modelDisponibilite.setRowCount(0);

        Creneaux creneauxDisponible = creneauService.selectCreneauByDate(planificationExamen);

        if (creneauxDisponible != null && creneauxDisponible.getCreneaux() != null) {
            ArrayList<Creneau> list = new ArrayList<>(creneauxDisponible.getCreneaux());
            list.sort(Comparator.comparing(Creneau :: getHeureDebut));
            if ( !list.isEmpty()){
                for (Creneau creneau : list){
                    modelDisponibilite.addRow(new Object[]{
                            creneau.getHeureDebut(),
                            creneau.getHeureFin(),
                    });
                }
            }else{
                JOptionPane.showMessageDialog(null, "Aucun créneau disponible.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    public JToolBar planningToolBar(){
        JToolBar bar = new JToolBar();

        JButton update = new JButton("Modifier");
        update.setFont(new Font("Arial", Font.PLAIN, 16));
        update.setBackground(new Color(127, 91, 255));
        JButton delete = new JButton("Supprimer");
        delete.setFont(new Font("Arial", Font.PLAIN, 16));
        delete.setBackground(new Color(255, 65, 65));
        JButton information = new JButton("Détail");
        information.setFont(new Font("Arial", Font.PLAIN, 16));

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tablePlanification.getSelectedRow();
                if (i >= 0){
                    PlanificationExamen plan = new PlanificationExamen();
                    plan.setIdPlanification(Integer.parseInt(modelPlanification.getValueAt(i, 0).toString()));
                    try {
                        plan = planificationService.selectOnePlanifications(plan);
                        planificationService.deletePlanification(plan);
                        chargerPlanification(planificationDuMedecin);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        information.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        bar.addSeparator(new Dimension( 10, 10));
        bar.add(information);
        bar.addSeparator(new Dimension( 10, 10));
        bar.add(update);
        bar.addSeparator(new Dimension( 10, 10));
        bar.add(delete);
        return bar;
    }

    public JToolBar disponibiliteToolBar(){
        JToolBar bar = new JToolBar();

        JButton addButton = new JButton("Réserver");
        addButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addButton.setBackground(new Color(151, 255, 110));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tableDisponibilite.getSelectedRow();
                if (i >= 0){
                    if(planificationDuMedecin.getDatePlanification().isBefore(LocalDate.now())){
                        JOptionPane.showMessageDialog(null, "La date sélectionnée est déjà passée. Veuillez choisir une date future.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }else{
                        planificationDuMedecin.setHeureDebut(LocalTime.parse(modelDisponibilite.getValueAt(i, 0).toString(), formatter));
                        planificationDuMedecin.setHeureFin(LocalTime.parse(modelDisponibilite.getValueAt(i, 1).toString(), formatter));
                        FrameInsertUpdatePlanification f = new FrameInsertUpdatePlanification(planificationDuMedecin, 1);
                    }
                }


            }
        });

        bar.add(addButton);
        return bar;
    }



}
