package edu.ezip.ing1.pds.graphics.medecin;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
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

public class PanelPlanningMedecin extends JPanel {

    private final static String LoggingLabel = "FrontEnd - HoraireService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final static MedecinService medecinService = new MedecinService(networkConfig);
    final static PlanificationWithNameService planificationWithNameService = new PlanificationWithNameService(networkConfig);
    final static CreneauService creneauService = new CreneauService(networkConfig);
    final static PlanificationService planificationService = new PlanificationService(networkConfig);

    private final String deleteFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\delete_button.png";
    private final String addFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\add_button.png";
    private final String updateFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\update_button.png";
    private final String informationFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\information_button.png";

    public static PlanificationExamen planificationDuMedecin = new PlanificationExamen();

    public static DefaultTableModel modelMedecin;
    public static JTable tableMedecin;
    public static DefaultTableModel modelPlanification;
    public static JTable tablePlanification;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    public static JRadioButton planningButton = new JRadioButton("Planning", true);
    public static JRadioButton disponibiliteButton = new JRadioButton("Disponibilité");
    public static DefaultTableModel modelDisponibilite;
    public static JTable tableDisponibilite;

    private static CardLayout rightCardLayout = new CardLayout();
    private static JPanel rightPanel = new JPanel(rightCardLayout);

    public PanelPlanningMedecin() throws IOException, InterruptedException {

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
        disponibiliteButton = new JRadioButton("Disponibilité");
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(tableauMedecin());
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
        valider.setBackground(new Color(165, 172, 250));
        valider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tableMedecin.getSelectedRow();
                if(i > 0){
                    planificationDuMedecin.setDatePlanification(calandrier.getDate());
                    planificationDuMedecin.setNumeroADELI(Integer.parseInt(modelMedecin.getValueAt(i, 0).toString()));

                    try {
                        if(planningButton.isSelected()){
                            chargerPlanification(planificationDuMedecin);
                        }else if (disponibiliteButton.isSelected()){
                            chargerDisponibilite(planificationDuMedecin);
                            logger.info("Planning" + planificationDuMedecin);
                        }

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        panel3.add(valider);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(panel3);
        return panel;
    }

    public static JPanel tableauMedecin() throws IOException, InterruptedException {
        JPanel panel = new JPanel();

        String[] columns = {"Numéro ADELI", "Nom et Prénom"};
        modelMedecin = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelMedecin.setRowCount(0);

        Medecins medecins = medecinService.selectAllMedecins();

        if (medecins != null && medecins.getMedecins() != null) {
            ArrayList<Medecin> list = new ArrayList<>(medecins.getMedecins());
            list.sort(Comparator.comparing(Medecin::getNom));//Order by nom
            planificationDuMedecin.setDatePlanification(LocalDate.now());
            planificationDuMedecin.setNumeroADELI(list.get(0).getNumeroADELI());
            for (Medecin medecin : list){
                modelMedecin.addRow(new Object[]{
                        medecin.getNumeroADELI(),
                        medecin.getNom()+"  "+medecin.getPrenom(),
                });
            }
        }

        tableMedecin = new JTable(modelMedecin);
        tableMedecin.setRowHeight(30);
        tableMedecin.setFont(new Font("Arial", Font.PLAIN, 15));
        tableMedecin.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tableMedecin);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        panel.add(scrollPane, BorderLayout.CENTER);

        int i = tableMedecin.getSelectedRow();
        if(i > 0){
            planificationDuMedecin.setNumeroADELI(Integer.parseInt(modelMedecin.getValueAt(i, 0).toString()));
        }
        return panel;
    }

    public JPanel rightPanelPlanning(){

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columns = {"ID","Heure de début", "Heure de Fin", "Nom Patient", "Nom Examen", "Numéro de Salle"};
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

        PlanificationWithNames planificationWithNames = planificationWithNameService.selectPlanificationWithNameByMedecin(planificationExamen);

        if (planificationWithNames != null && planificationWithNames.getPlanificationWithNames() != null) {
            ArrayList<PlanificationWithName> list = new ArrayList<>(planificationWithNames.getPlanificationWithNames());
            list.sort(Comparator.comparing(PlanificationWithName::getHeureDebut));              //Order by nom
            if (!list.isEmpty()) {
                for (PlanificationWithName plan : list) {
                    modelPlanification.addRow(new Object[]{
                            plan.getIdPlanification(),
                            plan.getHeureDebut(),
                            plan.getHeureFin(),
                            plan.getNomPatient() + "  " + plan.getPrenomPatient(),
                            plan.getNomExamen(),
                            plan.getNumeroSalle()
                    });
                }
            }else{

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

        Creneaux creneauxDisponible = creneauService.selectCreneauByDateByMedecin(planificationExamen);

        if (creneauxDisponible != null && creneauxDisponible.getCreneaux() != null) {
            ArrayList<Creneau> list = new ArrayList<>(creneauxDisponible.getCreneaux());
            list.sort(Comparator.comparing(Creneau :: getHeureDebut));              //Order by nom
            if ( !list.isEmpty()){
                for (Creneau creneau : list){
                    modelDisponibilite.addRow(new Object[]{
                            creneau.getHeureDebut(),
                            creneau.getHeureFin(),
                    });
                }
            }else {

            }
        }
    }


    public JToolBar planningToolBar(){
        JToolBar bar = new JToolBar();

        ImageIcon updateImage = new ImageIcon(updateFileNameButton);
        Image u = updateImage.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        updateImage = new ImageIcon(u);
        JButton update = new JButton(updateImage);

        ImageIcon deleteImage = new ImageIcon(deleteFileNameButton);
        Image d = deleteImage.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        deleteImage = new ImageIcon(d);
        JButton delete = new JButton(deleteImage);

        ImageIcon informationImage = new ImageIcon(informationFileNameButton);
        Image i = informationImage.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        deleteImage = new ImageIcon(i);
        JButton information = new JButton(deleteImage);

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

        update.setToolTipText("Modifier");
        delete.setToolTipText("Supprimer");
        information.setToolTipText("Plus d'information");
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

        ImageIcon addImage = new ImageIcon(addFileNameButton);
        Image a = addImage.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        addImage = new ImageIcon(a);
        JButton addButton = new JButton("Réserver", addImage);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tableDisponibilite.getSelectedRow();
                if (i > 0){
                    if(planificationDuMedecin.getDatePlanification().isBefore(LocalDate.now())){
                        JOptionPane.showMessageDialog(null, "La date sélectionnée est déjà passée. Veuillez choisir une date future.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }else{
                        planificationDuMedecin.setHeureDebut(LocalTime.parse(modelDisponibilite.getValueAt(i, 0).toString(), formatter));
                        planificationDuMedecin.setHeureFin(LocalTime.parse(modelDisponibilite.getValueAt(i, 1).toString(), formatter));
                        FrameInsertUpdatePlanification f = new FrameInsertUpdatePlanification(planificationDuMedecin);
                    }
                }


            }
        });

        addButton.setBackground(new Color(113, 70, 255));

        bar.add(addButton);
        return bar;
    }



}
