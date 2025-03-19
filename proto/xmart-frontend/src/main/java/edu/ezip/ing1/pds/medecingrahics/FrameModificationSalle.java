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

public class FrameModificationSalle extends JFrame {


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
    private static Salle salle;
    public FrameModificationSalle(Salle s){

        super("Création Salle");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(formulaire());
        add(boutons(), BorderLayout.SOUTH);

        setVisible(true);
        FrameModificationSalle.salle = s;
    }

    public JPanel formulaire(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 5, 5));
        numeroSalle = new JLabel("Numéro Salle : ");
        typeSalle = new JLabel("Type Salle : ");
        statut = new JLabel("Statut");
        System.out.println(FrameModificationSalle.salle.getNumeroSalle());
        System.out.println(FrameModificationSalle.salle.getTypeSalle());
//        valueNumero = new JTextField(FrameModificationSalle.salle.getNumeroSalle());
//        valueType = new JTextField(FrameModificationSalle.salle.getTypeSalle());

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
                Salle newSalle = new Salle(numero, type, statut);
                try {
                    salleService.deleteSalle(salle);
                    salleService.insertSalle(salle);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                FrameModificationSalle.this.dispose();

            }
        });

        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameModificationSalle.this.dispose();
            }
        });


        return panel;
    }
}
