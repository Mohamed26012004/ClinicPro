package edu.ezip.ing1.pds.graphics.salle;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.planning.SalleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;


public class PanelManipulationSalle extends JPanel {

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final static SalleService salleService = new SalleService(networkConfig);

    private final String deleteFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\delete_button.png";
    private final String addFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\add_button.png";
    private final String updateFileNameButton = "C:\\Users\\Maxime\\Documents\\apprendmaven\\ClinicPro\\proto\\xmart-frontend\\src\\main\\resources\\update_button.png";
    private final String statutReserve = "Réservé";
    private final String msgImposSupprime = "Salle Réservée !!!!!\nImpossiblle de la supprimer\nSupprimer le rendez-vous ou la planification qui a réservé la salle.";

    private static DefaultTableModel model;
    private JTable table;


    public PanelManipulationSalle() throws IOException, InterruptedException {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(this.toolBar(), BorderLayout.NORTH);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JLabel l = new JLabel("LISTE DES SALLES");
        l.setFont(new Font("Arial", Font.BOLD, 17));
        panel.add(l, BorderLayout.NORTH);

        String[] columns = {"ID", "Numéro de la Salle", "Type de salle", "Statut"};
        model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        chargerSalles();
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

    }


    public static void chargerSalles() throws IOException, InterruptedException {
        model.setRowCount(0);

        Salles salles = salleService.selectSalles();

        if (salles != null && salles.getSalles() != null) {
            ArrayList<Salle> list = new ArrayList<>(salles.getSalles());
            list.sort(Comparator.comparing(Salle::getNumeroSalle));              //Order by nom
            for (Salle salle : list){
                model.addRow(new Object[]{
                        salle.getId(),
                        salle.getNumeroSalle(),
                        salle.getTypeSalle(),
                        salle.getStatut()
                });
            }
        }
    }

    public JToolBar toolBar(){
        JToolBar bar = new JToolBar();

        ImageIcon addImage = new ImageIcon(addFileNameButton);
        Image i = addImage.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        addImage = new ImageIcon(i);
        JButton addButton = new JButton("Nouvelle Salle", addImage);

        ImageIcon updateImage = new ImageIcon(updateFileNameButton);
        Image u = updateImage.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        updateImage = new ImageIcon(u);
        JButton update = new JButton(updateImage);

        ImageIcon deleteImage = new ImageIcon(deleteFileNameButton);
        Image d = deleteImage.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        deleteImage = new ImageIcon(d);
        JButton delete = new JButton(deleteImage);


        delete.addActionListener(new ActionListener() {
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

                       if (salle.getStatut().equals(statutReserve)){
                            JOptionPane.showMessageDialog(null, msgImposSupprime, "Erreur", JOptionPane.ERROR_MESSAGE);
                       }else {
                            salleService.deleteSalle(salle);
                            chargerSalles();
                            JOptionPane.showMessageDialog(null, "Salle supprimée", "Message", JOptionPane.INFORMATION_MESSAGE);
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

                        Salle salle = new Salle();
                        salle.setId(Integer.parseInt(model.getValueAt(i, 0).toString()));
                        salle.setNumeroSalle(model.getValueAt(i, 1).toString());
                        salle.setTypeSalle(model.getValueAt(i, 2).toString());
                        salle.setStatut(model.getValueAt(i, 3).toString());

                        FrameCreationSalle f = new FrameCreationSalle(salle);
                        chargerSalles();
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
                    FrameCreationSalle f = new FrameCreationSalle(null);
                    chargerSalles();
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

