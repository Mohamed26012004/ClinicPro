package edu.ezip.ing1.pds.graphics.medecin;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.services.planning.MedecinService;
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

    public static PlanificationExamen planificationDuMedecin = new PlanificationExamen();

    public static DefaultTableModel modelMedecin;
    public static JTable tableMedecin;
    public static DefaultTableModel modelPlanification;
    public static JTable tablePlanification;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public PanelPlanningMedecin() throws IOException, InterruptedException {
//        examenDuMedecin.setNumeroADELI(0);
//        examenDuMedecin.setDatePlanification(LocalDate.now().minusDays(1));

    setLayout(new BorderLayout());
    add(leftPanel(), BorderLayout.WEST);
    add(rightPanel(planificationDuMedecin), BorderLayout.CENTER);
    }


    public static JPanel leftPanel() throws IOException, InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(null);
        JPanel panel1 = new JPanel();
        // Planning ou disponiblité
        JRadioButton planningButton = new JRadioButton("Planning", true);
        JRadioButton disponibiliteButton = new JRadioButton("Disponibilité");

        ButtonGroup group = new ButtonGroup();
        group.add(planningButton);
        group.add(disponibiliteButton);

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
                logger.info("Date sélectionnée " +chooseDate);
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
                    planificationDuMedecin.setNumeroADELI(Integer.parseInt(modelMedecin.getValueAt(i, 0).toString()));

                    try {
                        logger.info("Date sélectionnée " +planificationDuMedecin);
                        chargerPlanification(planificationDuMedecin);
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

    public static JPanel rightPanel(PlanificationExamen planificationExamen) throws IOException, InterruptedException {

        JPanel panel = new JPanel();
        panel.setBorder(null);



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

        return panel;
    }

    public static void chargerPlanification(PlanificationExamen planificationExamen) throws IOException, InterruptedException {
        modelPlanification.setRowCount(0);

        PlanificationWithNames planificationWithNames = planificationWithNameService.selectPlanificationWithNameByMedecin(planificationExamen);

        if (planificationWithNames != null && planificationWithNames.getPlanificationWithNames() != null) {
            ArrayList<PlanificationWithName> list = new ArrayList<>(planificationWithNames.getPlanificationWithNames());
            list.sort(Comparator.comparing(PlanificationWithName :: getHeureDebut));              //Order by nom
            for (PlanificationWithName plan : list){
                modelPlanification.addRow(new Object[]{
                        plan.getIdPlanification(),
                        plan.getHeureDebut(),
                        plan.getHeureFin(),
                        plan.getNomPatient()+"  "+plan.getPrenomPatient(),
                        plan.getNomExamen(),
                        plan.getNumeroSalle()
                });
            }
        }
    }
}
