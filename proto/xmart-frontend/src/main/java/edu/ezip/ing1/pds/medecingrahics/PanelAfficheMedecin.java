package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.business.dto.Medecins;
import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.business.dto.Salles;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesplanning.MedecinService;
import edu.ezip.ing1.pds.servicesplanning.SalleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;


public class PanelAfficheMedecin extends JPanel {


    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final static MedecinService medecinService = new MedecinService(networkConfig);

    private final String deleteFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\delete_button.png";
    private final String addFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\add_button.png";
    private final String updateFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\update_button.png";
    private final String informationFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\information_button.png";

    private final String msgImposSupprime = "";

    private static JButton addButton;
    private static JButton update;
    private static JButton delete;
    private static JButton information;
    private static JScrollPane scrollPane;
    private static JPanel panneau = new JPanel(new BorderLayout());


    private static DefaultTableModel model;
    private JTable table;


    public PanelAfficheMedecin() throws IOException, InterruptedException {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(this.toolBar(), BorderLayout.NORTH);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JLabel l = new JLabel("LISTE DES MEDECINS");
        l.setFont(new Font("Arial", Font.BOLD, 17));
        panel.add(l, BorderLayout.NORTH);

        String[] columns = {"Numéro ADELI", "Nom", "Prénom", "Téléphone", "Spécialité", "Salaire"};
        model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        chargerMedecins();
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

    }


    public static void chargerMedecins() throws IOException, InterruptedException {
        model.setRowCount(0);

        Medecins medecins = medecinService.selectAllMedecins();

        if (medecins != null && medecins.getMedecins() != null) {
            ArrayList<Medecin> list = new ArrayList<>(medecins.getMedecins());
            list.sort(Comparator.comparing(Medecin::getNom));              //Order by nom
            for (Medecin medecin : list){
                model.addRow(new Object[]{
                        medecin.getNumeroADELI(),
                        medecin.getNom(),
                        medecin.getPrenom(),
                        medecin.getTelephone(),
                        medecin.getSpecialite(),
                        medecin.getSalaire()
                });
            }
        }
    }

    public JToolBar toolBar(){
        JToolBar bar = new JToolBar();

        ImageIcon addImage = new ImageIcon(addFileNameButton);
        Image a = addImage.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        addImage = new ImageIcon(a);
        JButton addButton = new JButton("Nouveau Médecin", addImage);

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

            }
        });
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int i = table.getSelectedRow();
                    if (i >= 0) {

                        Salle salle = new Salle();
                        salle.setId(Integer.parseInt(model.getValueAt(i, 0).toString()));
                        salle.setNumeroSalle(model.getValueAt(i, 1).toString());
                        salle.setTypeSalle(model.getValueAt(i, 2).toString());
                        salle.setStatut(model.getValueAt(i, 3).toString());

                        FrameCreationSalle f = new FrameCreationSalle(salle);
                        JOptionPane.showMessageDialog(null, "Mise à jour effectuée.", "Message", JOptionPane.INFORMATION_MESSAGE);
                        chargerMedecins();
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        addButton.setBackground(new Color(113, 70, 255));
        update.setToolTipText("Modifier");
        delete.setToolTipText("Supprimer");
        information.setToolTipText("Information");
        bar.add(addButton);
        bar.addSeparator(new Dimension( 10, 10));
        bar.add(information);
        bar.addSeparator(new Dimension( 10, 10));
        bar.add(update);
        bar.addSeparator(new Dimension( 10, 10));
        bar.add(delete);
        return bar;
    }



}
