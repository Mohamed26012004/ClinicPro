package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Consulte;
import edu.ezip.ing1.pds.business.dto.Horaire;
import edu.ezip.ing1.pds.business.dto.Horaires;
import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesplanning.HoraireService;
import edu.ezip.ing1.pds.servicesplanning.MedecinService;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InsertUpdateMedecinWindows extends JFrame {

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    static final HoraireService horaireService = new HoraireService(networkConfig);
    static final MedecinService medecinService = new MedecinService(networkConfig);


    private JPanel contentPane ;
    private JLabel numero = new JLabel("Numéro ADELI : ");
    private JLabel nom = new JLabel("Nom : ");
    private JLabel prenom = new JLabel("Prénom : ");
    private JLabel telephone = new JLabel("Téléphone : ");
    private JLabel specialite = new JLabel("Spécialité : ");
    private JLabel salaire = new JLabel("Salaire : ");
    JTextField valueNumero = new JTextField("");
    JTextField valueNom = new JTextField("");
    JTextField valuePrenom = new JTextField("");
    JTextField valueTelephone = new JTextField("");
    JTextField valueSpecialite = new JTextField("");
    JTextField valueSalaire = new JTextField("");
    private JPanel panneau;
    private JPanel sousPanneau = new JPanel();
    private static JPanel panelHoraireDejaExistant = new JPanel();
    private static ArrayList<PanelHoraire> listHoraireAajouter = new ArrayList<>();
    private ArrayList<Horaire> horaieAajouter = new ArrayList<>();
    private ArrayList<Horaire> listeHoraireDejaExistant = new ArrayList<>();
    private Horaires horaireMedecin = new Horaires();
    private static Horaires horaireDejaExistant;
    private Medecin medecin;
    static {
        try {
            horaireDejaExistant = horaireService.selectHoraires();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


    public InsertUpdateMedecinWindows(Medecin medecin) throws IOException, InterruptedException {

        super("Médécins");
        horaireMedecin = horaireService.selectHoraireMedecin(medecin);

        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.add(formulaire(medecin));
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel label = new JLabel("Horaires");
        JPanel pan = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pan.add(label);
        contentPane.add(pan);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(panelHorairesMedecin(medecin));
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPane.add(southButtons(medecin));
        setVisible(true);
        this.medecin = medecin;
    }

    public JPanel formulaire(Medecin medecin){
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        if (medecin != null){
            valueNumero = new JTextField(String.valueOf(medecin.getNumeroADELI()));
            valueNom = new JTextField(medecin.getNom());
            valuePrenom = new JTextField(medecin.getPrenom());
            valueTelephone = new JTextField(medecin.getTelephone());
            valueSpecialite = new JTextField(medecin.getSpecialite());
            valueSalaire = new JTextField(String.valueOf(medecin.getSalaire()));

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
        return panel;
    }
    /*
    Panneau principal situé sous Horaire, contenant les Horaires du médécins ou le formulaire d'ajout.
     */
    public JPanel panelHorairesMedecin(Medecin medecin){
        panneau = new JPanel(new BorderLayout());
        panneau.setBorder(new EmptyBorder(15, 15, 15, 15));
        panneau.add(boutonHoraireMedecin(), BorderLayout.WEST);
        panneau.add(afficheListHoraireAajouter(medecin), BorderLayout.CENTER);

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
                    JOptionPane.showMessageDialog(null, "Le Numéro ADELI n'est constitué que de chiffres, veillez rectifier", "Numéro ADELI", JOptionPane.INFORMATION_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Veillez entrer un nombre entier (positif) pour le salaire", "Erreur sur Salaire", JOptionPane.INFORMATION_MESSAGE);
                    throw new RuntimeException(ex);
                }

                if(numeroADELI<0 || salaire<0){
                    JOptionPane.showMessageDialog(null, "Rectifier le numéro ADELI ou le salaire en mettant des valeurs positives ", "Erreur sur Salaire ou numéro ADELI", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    Medecin medecin = new Medecin(numeroADELI, nom, prenom, telephone, specialite, salaire);
                    try {
                        medecinService.insertMedecin(medecin);
                        for(PanelHoraire p : listHoraireAajouter){
                            Horaire h = horaireService.selectOneHoraire(p.horaireOfPanel());
                            Consulte c = new Consulte(medecin.getNumeroADELI(), h.getId());
                            medecinService.insertConsulte(c);
                        }
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                InsertUpdateMedecinWindows.this.dispose();
            }
        });


        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InsertUpdateMedecinWindows.this.dispose();
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

        JButton ajouter = new JButton("Ajouter");
        JButton supprimer = new JButton("Supprimer");
        JButton creer = new JButton("Créer");
        creer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panneau.removeAll();
                panneau.add(boutonHoraireMedecin(), BorderLayout.WEST);
                panneau.add(formulaireCreerHoraire(), BorderLayout.CENTER);
                panneau.repaint();
                panneau.revalidate();
            }
        });
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panneau.removeAll();
                panneau.add(boutonHoraireMedecin(), BorderLayout.WEST);
                panneau.add(methodeHoraireDejaExistant(medecin), BorderLayout.CENTER);
                panneau.repaint();
                panneau.revalidate();
            }
        });
        supprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelHoraire panelHoraire = PanelHoraire.panelHoraireCliquer;
                listHoraireAajouter.remove(panelHoraire);
                try {
                    horaireService.deleteHoraire(panelHoraire.horaireOfPanel());
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                panneau.removeAll();
                panneau.add(boutonHoraireMedecin(), BorderLayout.WEST);
                panneau.add(afficheListHoraireAajouter(medecin), BorderLayout.CENTER);
                panneau.repaint();
                panneau.revalidate();
            }
        });

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
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(3, 2, 5, 5));
        pane.setBorder(new EmptyBorder(15, 15, 15, 15));
        String[] tab = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        JComboBox<String> jour = new JComboBox<>(tab);
        JLabel day = new JLabel("Jour");
        JLabel biginHour = new JLabel("Heure de début (HH:MM)");
        JLabel endHour = new JLabel("Heure de fin (HH:MM)");
        JTextField debut = new JTextField("");
        JTextField fin = new JTextField("");
        pane.add(day);
        pane.add(jour);
        pane.add(biginHour);
        pane.add(debut);
        pane.add(endHour);
        pane.add(fin);

        JButton ajouter = new JButton("Ajouter");
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jourSemaine = (String)jour.getSelectedItem();
                LocalTime heureDebut = LocalTime.parse(debut.getText(), formatter);
                LocalTime heureFin = LocalTime.parse(fin.getText(),  formatter);
                Horaire horaire = new Horaire(jourSemaine, heureDebut, heureFin);
                PanelHoraire p = new PanelHoraire(horaire);
                try {
                    horaireService.insertHoraire(horaire);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if(!listHoraireAajouter.contains(p)){
                    listHoraireAajouter.add(p);
                }
                panneau.removeAll();
                panneau.add(boutonHoraireMedecin(), BorderLayout.WEST);
                panneau.add(afficheListHoraireAajouter(medecin), BorderLayout.CENTER);
                panneau.repaint();
                panneau.revalidate();
            }
        });
        JPanel panelBtn = new JPanel(new FlowLayout());
        panelBtn.add(ajouter);

        panel.add(pane);
        panel.add(panelBtn, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel methodeHoraireDejaExistant(Medecin medecin){
        panelHoraireDejaExistant.setLayout(new BorderLayout());
        panelHoraireDejaExistant.removeAll();
        JPanel p = new JPanel(new FlowLayout());
        p.add(new JLabel("Horaire déjà existant"));
        panelHoraireDejaExistant.add(p, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(400, 400));
        for (Horaire h : horaireDejaExistant.getHoraires()){
            PanelHoraire panelHoraire = new PanelHoraire(h);
            panel.add(panelHoraire);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        panelHoraireDejaExistant.add(scrollPane, BorderLayout.CENTER);

        JPanel pan = new JPanel(new FlowLayout());
        JButton ajouter = new JButton("Ajouter");
        pan.add(ajouter);
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelHoraire panelHoraire = (PanelHoraire) PanelHoraire.panelHoraireCliquer;
                if(!listHoraireAajouter.contains(panelHoraire)){
                    listHoraireAajouter.add(panelHoraire);
                }
                panneau.removeAll();
                panneau.add(boutonHoraireMedecin(), BorderLayout.WEST);
                panneau.add(afficheListHoraireAajouter(medecin), BorderLayout.CENTER);
                panneau.repaint();
                panneau.revalidate();
            }
        });
        panelHoraireDejaExistant.add(pan, BorderLayout.SOUTH);
        panelHoraireDejaExistant.repaint();
        panelHoraireDejaExistant.revalidate();
        return panelHoraireDejaExistant;
    }

    public JPanel afficheListHoraireAajouter(Medecin medecin){
        JPanel pane = new JPanel(new BorderLayout());

        pane.removeAll();
        JPanel p = new JPanel(new FlowLayout());
        p.add(new JLabel("Horaire à ajouter au médecin"));
        pane.add(p, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(150, 150));
        for (PanelHoraire h : listHoraireAajouter){
            panel.add(h);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        pane.add(scrollPane, BorderLayout.CENTER);

        JPanel pan = new JPanel(new FlowLayout());
        JButton ajouter = new JButton("Ajouter");
        pan.add(ajouter);
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelHoraire panelHoraire = (PanelHoraire) PanelHoraire.panelHoraireCliquer;
                listHoraireAajouter.add(panelHoraire);
            }
        });
        pane.add(pan, BorderLayout.SOUTH);
        pane.repaint();
        pane.revalidate();
        return pane;
    }

}
