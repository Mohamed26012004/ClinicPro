package edu.ezip.ing1.pds.graphics.medecin;

import edu.ezip.ing1.pds.business.dto.Horaire;
import edu.ezip.ing1.pds.business.dto.Horaires;
import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesplanning.HoraireService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class PanelInformationMedecin extends JFrame{

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    static final HoraireService horaireService = new HoraireService(networkConfig);

    private JPanel contentPane ;

    private JPanel panneau;
    private CardLayout cardLayoutPanneauCenter = new CardLayout();
    private JPanel panneauCenter = new JPanel(cardLayoutPanneauCenter);
    private static ArrayList<Horaire> listHoraireAajouter = new ArrayList<>();
    private Horaires horaireMedecin = new Horaires();
    private Medecin medecin;

    private DefaultTableModel modelHoraireAajouter;
    private JTable tableHoraireAajouter;
    private Medecin medecinToUpdate;

    public PanelInformationMedecin(Medecin medecin) throws IOException, InterruptedException {

        super("Medecin");
        setSize(800, 600);
        this.medecinToUpdate = medecin;
        //Vider la liste car c'est une variable statique, afin qu'elle ne contienne pas les éléments d'une précédentes instanciation
        listHoraireAajouter.clear();
        String[] columns = {"Jour", "Heure de début", "Heure de Fin"};
        modelHoraireAajouter = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHoraireAajouter = new JTable(modelHoraireAajouter);

        tableHoraireAajouter.setRowHeight(30);
        tableHoraireAajouter.setFont(new Font("Arial", Font.PLAIN, 15));

        horaireMedecin = horaireService.selectHoraireMedecin(medecin);

        contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
//        setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(panelHorairesMedecin(medecin));
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        this.medecin = medecin;
    }


    public JPanel panelHorairesMedecin(Medecin medecin) throws IOException, InterruptedException {
        panneau = new JPanel(new BorderLayout());
        panneau.setBorder(new EmptyBorder(15, 15, 15, 15));
        panneauCenter.add(afficheListHoraireAajouter(medecin), "HoraireAajouter");

//        panneauCenter.add(afficheListHoraireAajouter(null), "HoraireAajouter2");

        panneau.add(panneauCenter, BorderLayout.CENTER);
        panneau.setBorder(BorderFactory.createTitledBorder("Horaires du médecin"));
        return panneau;
    }


    public JPanel afficheListHoraireAajouter(Medecin medecin) throws IOException, InterruptedException {
        JPanel pane = new JPanel(new BorderLayout());

        modelHoraireAajouter.setRowCount(0);

        if(medecin != null){
            Horaires horaires = horaireService.selectHoraireMedecin(medecin);
            ArrayList<Horaire> listHoraire = new ArrayList<>(horaires.getHoraires());
            setListHoraireAajouter(listHoraire);
            listHoraire.sort(Comparator.comparing(Horaire :: getJour));
            if (!listHoraire.isEmpty()){
                for (Horaire h : listHoraire){
                    modelHoraireAajouter.addRow(new Object[]{
                            h.getJour(), h.getHeureDebut(), h.getHeureFin()
                    });
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(tableHoraireAajouter);
        pane.add(scrollPane, BorderLayout.CENTER);
        return pane;
    }

    public static ArrayList<Horaire> getListHoraireAajouter() {
        return listHoraireAajouter;
    }

    public static void setListHoraireAajouter(ArrayList<Horaire> listHoraireAajouter) {
        PanelInformationMedecin.listHoraireAajouter = listHoraireAajouter;
    }


    public void setMedecinToUpdate(Medecin medecinToUpdate) {
        this.medecinToUpdate = medecinToUpdate;
    }

}
