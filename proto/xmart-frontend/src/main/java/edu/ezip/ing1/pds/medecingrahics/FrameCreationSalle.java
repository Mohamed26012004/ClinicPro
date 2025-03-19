package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.servicesplanning.SalleService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FrameCreationSalle extends JFrame {

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final SalleService salleService = new SalleService(networkConfig);

    private JLabel numeroSalle;
    private JLabel typeSalle;
    private JLabel statut;
    private JTextField valueNumero;
    private JTextField valueType;
    private String[] tab = {"Libre", "Réservé", "En maintenance"};
    private JComboBox<String> boxStatut = new JComboBox<>(tab);
    private JButton enregistrer;
    private JButton annuler;

    public FrameCreationSalle(){
        super("Création Salle");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(formulaire());
        add(boutons(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public JPanel formulaire(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 5, 5));
        numeroSalle = new JLabel("Numéro Salle : ");
        typeSalle = new JLabel("Type Salle : ");
        statut = new JLabel("Statut");
        valueNumero = new JTextField("");
        valueType = new JTextField("");

        panel.add(numeroSalle);
        panel.add(valueNumero);
        panel.add(typeSalle);
        panel.add(valueType);
        panel.add(statut);
        panel.add(boxStatut);

        return panel;
    }

    public JPanel boutons(){
        JPanel panel = new JPanel(new FlowLayout());
        enregistrer = new JButton("Enregistrer");
        annuler = new JButton("Annuler");

        panel.add(enregistrer);
        panel.add(annuler);

        enregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numero = valueNumero.getText();
                String type = valueType.getText();
                String statut = (String) boxStatut.getSelectedItem();

                Salle salle = new Salle(numero, type, statut);
                try {
                    PanelManipulationSalle.SalleDejaExistantes().removeAll();
                    salleService.insertSalle(salle);
                    PanelManipulationSalle.SalleDejaExistantes().repaint();
                    PanelManipulationSalle.SalleDejaExistantes().revalidate();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                FrameCreationSalle.this.dispose();

            }
        });

        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameCreationSalle.this.dispose();
            }
        });


        return panel;
    }
}
