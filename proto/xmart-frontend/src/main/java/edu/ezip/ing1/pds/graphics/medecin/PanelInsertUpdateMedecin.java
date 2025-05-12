package edu.ezip.ing1.pds.graphics.medecin;

import edu.ezip.ing1.pds.business.dto.Consulte;
import edu.ezip.ing1.pds.business.dto.Horaire;
import edu.ezip.ing1.pds.business.dto.Horaires;
import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.services.planning.HoraireService;
import edu.ezip.ing1.pds.services.planning.MedecinService;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class PanelInsertUpdateMedecin extends JFrame{

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    static final HoraireService horaireService = new HoraireService(networkConfig);
    static final MedecinService medecinService = new MedecinService(networkConfig);


    private JPanel contentPane ;
    private JLabel numero = Fenetre.createLabel("Numéro ADELI : ");
    private JLabel nom = Fenetre.createLabel("Nom : ");
    private JLabel prenom = Fenetre.createLabel("Prénom : ");
    private JLabel telephone = Fenetre.createLabel("Téléphone : ");
    private JLabel specialite = Fenetre.createLabel("Spécialité : ");
    private JLabel salaire = Fenetre.createLabel("Salaire : ");
    private JTextField valueNumero = Fenetre.createTextField("");
    private JTextField valueNom = Fenetre.createTextField("");
    private JTextField valuePrenom = Fenetre.createTextField("");
    private JTextField valueTelephone = Fenetre.createTextField("");
    private JTextField valueSpecialite = Fenetre.createTextField("");
    private JTextField valueSalaire = Fenetre.createTextField("");
    private JPanel panneau;
    private CardLayout cardLayoutPanneauCenter = new CardLayout();
    private JPanel panneauCenter = new JPanel(cardLayoutPanneauCenter);
    private static JPanel panelHoraireDejaExistant = new JPanel();
    private static ArrayList<Horaire> listHoraireAajouter = new ArrayList<>();
    private Horaires horaireMedecin = new Horaires();
    private static Horaires horaireDejaExistant;
    private Medecin medecin;
    private final String[] tabJour = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
    private final String[] tabHeure = {"00:00 - 01:00", "01:00 - 02:00", "02:00 - 03:00", "03:00 - 04:00", "04:00 - 05:00", "05:00 - 06:00","06:00 - 07:00", "07:00 - 08:00", "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00",
            "11:00 - 12:00","12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00","18:00 - 19:00", "19:00 - 20:00", "20:00 - 21:00", "21:00 - 22:00", "22:00 - 23:00", "23:00 - 00:00"};

    private DefaultTableModel modelHoraireAajouter;
    private JTable tableHoraireAajouter;
    private DefaultTableModel modelHoraireExistant;
    private JTable tableHoraireExistant;
    private Medecin medecinToUpdate;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


    public PanelInsertUpdateMedecin(Medecin medecin) throws IOException, InterruptedException {

        super("Medecin");
        setSize(800, 500);
        this.medecinToUpdate = medecin;
        //Vider la liste car c'est une variable statique, afin qu'elle ne contienne pas les éléments d'une précédentes instanciation
        listHoraireAajouter.clear();
        String[] columns = {"Jour", "Heure de début", "Heure de Fin"};
        modelHoraireExistant = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelHoraireAajouter = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHoraireExistant = new JTable(modelHoraireExistant);
        tableHoraireAajouter = new JTable(modelHoraireAajouter);

        tableHoraireExistant.setRowHeight(30);
        tableHoraireExistant.setFont(new Font("Arial", Font.PLAIN, 15));
        tableHoraireAajouter.setRowHeight(30);
        tableHoraireAajouter.setFont(new Font("Arial", Font.PLAIN, 15));

        horaireMedecin = horaireService.selectHoraireMedecin(medecin);

        contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
//        setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.add(formulaire(medecin));
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(panelHorairesMedecin(medecin));
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPane.add(southButtons(medecin));

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        this.medecin = medecin;
    }

    public JPanel formulaire(Medecin medecin){
        JPanel panel = new JPanel(new GridLayout(2, 6, 5, 8));

        if (medecin != null){
            valueNumero = Fenetre.createTextField(String.valueOf(medecin.getNumeroADELI()));
            valueNom = Fenetre.createTextField(medecin.getNom());
            valuePrenom = Fenetre.createTextField(medecin.getPrenom());
            valueTelephone = Fenetre.createTextField(medecin.getTelephone());
            valueSpecialite = Fenetre.createTextField(medecin.getSpecialite());
            valueSalaire = Fenetre.createTextField(String.valueOf(medecin.getSalaire()));

        }else{
            valueNumero = Fenetre.createTextField("");
            valueNom = Fenetre.createTextField("");
            valuePrenom = Fenetre.createTextField("");
            valueTelephone = Fenetre.createTextField("");
            valueSpecialite = Fenetre.createTextField("");
            valueSalaire = Fenetre.createTextField("");
        }
        panel.add(numero);
        panel.add(valueNumero);
        panel.add(nom);
        panel.add(valueNom);
        panel.add(prenom);
        panel.add(valuePrenom);
        panel.add(telephone);
        panel.add(valueTelephone);
        panel.add(specialite);
        panel.add(valueSpecialite);
        panel.add(salaire);
        panel.add(valueSalaire);
        panel.setBorder(BorderFactory.createTitledBorder("Information du médecin"));
        return panel;
    }
    /*
    Panneau principal situé sous Horaire, contenant les Horaires du médécins ou le formulaire d'ajout.
     */
    public JPanel panelHorairesMedecin(Medecin medecin) throws IOException, InterruptedException {
        panneau = new JPanel(new BorderLayout());
        panneau.setBorder(new EmptyBorder(15, 15, 15, 15));
        panneau.add(boutonHoraireMedecin(), BorderLayout.WEST);

        panneauCenter.add(afficheListHoraireAajouter(medecin), "HoraireAajouter");
        panneauCenter.add(formulaireCreerHoraire(), "Formulaire");
        panneauCenter.add(methodeHoraireDejaExistant(), "HoraireExistante");
//        panneauCenter.add(afficheListHoraireAajouter(null), "HoraireAajouter2");

        panneau.add(panneauCenter, BorderLayout.CENTER);
        panneau.setBorder(BorderFactory.createTitledBorder("Horaires du médecin"));
        return panneau;
    }

    //Méthode contant les boutons d'enregistrement, de suppression...

    public JPanel southButtons(Medecin m){
        JPanel panel = new JPanel(new FlowLayout());
        JButton enregistrer =  new JButton("Enregistrer");
        JButton modifier = new JButton("Modifier");
        JButton annuler = new JButton("Annuler");

        enregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numeroADELI;
                try{
                    numeroADELI = Integer.parseInt(valueNumero.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Le numéro ADELI ne doit contenir que des chiffres. Merci de bien vouloir le rectifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                String nom = valueNom.getText();
                String prenom = valuePrenom.getText();
                String telephone= valueTelephone.getText();
                String specialite = valueSpecialite.getText();
                int salaire;
                try {
                    salaire = Integer.parseInt(valueSalaire.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Merci de renseigner un nombre entier strictement positif pour le salaire.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }

                if(numeroADELI<0 || salaire<0){
                    JOptionPane.showMessageDialog(null, "Le numéro ADELI et le salaire doivent être des valeurs positives.\nVeuillez effectuer les corrections nécessaires. ", "Erreur", JOptionPane.ERROR_MESSAGE);
                }else{
                    Medecin medecin = new Medecin(numeroADELI, nom, prenom, telephone, specialite, salaire);
                    try {
                        medecinService.insertMedecin(medecin);
                        for(Horaire horaire  : listHoraireAajouter){
                            Horaire h = horaireService.selectOneHoraire(horaire);
                            Consulte c = new Consulte(medecin.getNumeroADELI(), h.getId());
                            medecinService.insertConsulte(c);
                            PanelInsertUpdateMedecin.this.dispose();
                        }
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }

            }
        });


        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelInsertUpdateMedecin.this.dispose();
            }
        });

        panel.add(enregistrer);
        panel.add(modifier);
        panel.add(annuler);
        return panel;
    }

    //Boutons au gauche permettant d'ajouter ou de supprimer une horaire.

    public JPanel boutonHoraireMedecin(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel ajouter = new JPanel();
        ajouter.add(Fenetre.createLabel("Sélectionner Horaire"));
        JPanel supprimer = new JPanel();
        supprimer.add(Fenetre.createLabel("Supprimer"));
        JPanel creer = new JPanel();
        creer.add(Fenetre.createLabel("Créer"));
        creer.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayoutPanneauCenter.show(panneauCenter, "Formulaire");
            }
        });
        ajouter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayoutPanneauCenter.show(panneauCenter, "HoraireExistante");
            }
        });
        supprimer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int i = tableHoraireAajouter.getSelectedRow();
                if(i >= 0){
                    Horaire h = new Horaire();
                    h.setJour(modelHoraireAajouter.getValueAt(i, 0).toString());
                    h.setHeureDebut(LocalTime.parse(modelHoraireAajouter.getValueAt(i, 1).toString(), formatter));
                    h.setHeureFin(LocalTime.parse(modelHoraireAajouter.getValueAt(i, 2).toString(), formatter));

                    try {
                        horaireService.deleteHoraire(h);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                cardLayoutPanneauCenter.show(panneauCenter, "HoraireAajouter");
            }
        });

        ajouter.setPreferredSize(ajouter.getMinimumSize());
        creer.setPreferredSize(creer.getMinimumSize());
        supprimer.setPreferredSize(supprimer.getMinimumSize());
        panel.add(ajouter);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(creer);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(supprimer);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        return panel;
    }

    /*Formulaire permettant d'ajouter une horaire au où elle n'existe pas
    Elle sera contenu dans le même panneau qui affichera toutes les horaires du médécin
     */
    public  JPanel formulaireCreerHoraire(){
        JPanel panel = new JPanel(new BorderLayout());
    //    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(3, 2, 5, 5));
        pane.setBorder(new EmptyBorder(15, 15, 15, 15));

        JComboBox<String> jour = new JComboBox<>(tabJour);
        JComboBox<String> creneaux = new JComboBox<>(tabHeure);
        JComboBox<String> heureF = new JComboBox<>(tabHeure);
        jour.setPreferredSize(new Dimension(50, 50));
        creneaux.setPreferredSize(new Dimension(50, 50));
        JLabel day = Fenetre.createLabel("Jour : ");
        day.setPreferredSize(new Dimension(50, 50));
        JLabel hour = Fenetre.createLabel("Créneau horaire : ");
        hour.setPreferredSize(new Dimension(50, 50));

        pane.add(day);
        pane.add(jour);
        pane.add(hour);
        pane.add(creneaux);


        JButton ajouter = new JButton("Ajouter");
        JButton retour = new JButton("Retour");
        retour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayoutPanneauCenter.show(panneauCenter, "HoraireAajouter");
            }
        });
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jourSemaine = (String)jour.getSelectedItem();
                String creneau = (String) Objects.requireNonNull(creneaux.getSelectedItem());
                LocalTime[] tabH = creneauToHeure(creneau);
                LocalTime heureDebut = tabH[0];
                LocalTime heureFin = tabH[1];
                Horaire horaire = new Horaire(jourSemaine, heureDebut, heureFin);

                if (heureDebut.isAfter(heureFin)){
                    JOptionPane.showMessageDialog(null, "L'heure de début doit être avant l'heure de fin.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }else {
                    try {
                        horaireService.insertHoraire(horaire);

                        if(!getListHoraireAajouter().contains(horaire)){
                            getListHoraireAajouter().add(horaire);
                        }
                        cardLayoutPanneauCenter.show(panneauCenter, "HoraireAajouter");
                    } catch (InterruptedException | IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });
        JPanel panelBtn = new JPanel(new FlowLayout());
        panelBtn.add(ajouter);
        panelBtn.add(retour);

        panel.add(pane);
        panel.add(panelBtn, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel methodeHoraireDejaExistant() throws IOException, InterruptedException {

        horaireDejaExistant = horaireService.selectHoraires();

        panelHoraireDejaExistant.setLayout(new BorderLayout());
        JPanel p = new JPanel(new FlowLayout());
        p.add(Fenetre.createLabel("Horaire déjà existant."));
        panelHoraireDejaExistant.add(p, BorderLayout.NORTH);

        modelHoraireExistant.setRowCount(0);

        if (horaireDejaExistant.getHoraires() != null && horaireDejaExistant != null){
            ArrayList<Horaire> list = new ArrayList<>(horaireDejaExistant.getHoraires());
            list.sort(Comparator.comparing(Horaire :: getJour));
            for (Horaire h : list){
                modelHoraireExistant.addRow(new Object[]{
                        h.getJour(), h.getHeureDebut(), h.getHeureFin()
                });
            }
        }
        JScrollPane scrollPane = new JScrollPane(tableHoraireExistant);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des horaires disponibles"));
        panelHoraireDejaExistant.add(scrollPane, BorderLayout.CENTER);

        JPanel pan = new JPanel(new FlowLayout());
        JButton ajouter = new JButton("Ajouter");
        JButton retour = new JButton("Retour");
        retour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayoutPanneauCenter.show(panneauCenter, "HoraireAajouter");
            }
        });
        pan.add(ajouter);
        pan.add(retour);
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tableHoraireExistant.getSelectedRow();
                if(i >= 0){
                    Horaire h = new Horaire();
                    h.setJour(modelHoraireExistant.getValueAt(i, 0).toString());
                    h.setHeureDebut(LocalTime.parse(modelHoraireExistant.getValueAt(i, 1).toString(), formatter));
                    h.setHeureFin(LocalTime.parse(modelHoraireExistant.getValueAt(i, 2).toString(), formatter));
                    if(!getListHoraireAajouter().contains(h)){
                        ArrayList <Horaire> list = getListHoraireAajouter();
                        list.add(h);
                        setListHoraireAajouter(list);
                    }
                    cardLayoutPanneauCenter.show(panneauCenter, "HoraireAajouter");

                }

            }
        });
        panelHoraireDejaExistant.add(pan, BorderLayout.SOUTH);
        return panelHoraireDejaExistant;
    }

    public JPanel afficheListHoraireAajouter(Medecin medecin) throws IOException, InterruptedException {
        JPanel pane = new JPanel(new BorderLayout());
        JPanel p = new JPanel(new FlowLayout());
        p.add(Fenetre.createLabel("Horaires à ajouter au médecin."));
        pane.add(p, BorderLayout.NORTH);

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
        }else {
            ArrayList<Horaire> list = new ArrayList<>(getListHoraireAajouter());
            list.sort(Comparator.comparing(Horaire :: getJour));
            if (!list.isEmpty()){
                for (Horaire h : list){
                    modelHoraireAajouter.addRow(new Object[]{
                            h.getJour(), h.getHeureDebut(), h.getHeureFin()
                    });
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(tableHoraireAajouter);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des horaires à ajouter"));
        pane.add(scrollPane, BorderLayout.CENTER);
        return pane;
    }

    public static ArrayList<Horaire> getListHoraireAajouter() {
        return listHoraireAajouter;
    }

    public static void setListHoraireAajouter(ArrayList<Horaire> listHoraireAajouter) {
        PanelInsertUpdateMedecin.listHoraireAajouter = listHoraireAajouter;
    }

    public Medecin getMedecinToUpdate() {
        return medecinToUpdate;
    }

    public void setMedecinToUpdate(Medecin medecinToUpdate) {
        this.medecinToUpdate = medecinToUpdate;
    }

    public static LocalTime[] creneauToHeure(String creneau){
        LocalTime[] tab = new LocalTime[2];
        String[] s = creneau.split(" - ");
        tab[0] = LocalTime.parse(s[0], formatter);
        tab[1] = LocalTime.parse(s[1], formatter);
        return tab;
    }
}
