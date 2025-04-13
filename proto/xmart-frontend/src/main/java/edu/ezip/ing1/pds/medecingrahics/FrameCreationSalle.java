package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.servicesplanning.SalleService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FrameCreationSalle extends JFrame {

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final SalleService salleService = new SalleService(networkConfig);

    private JLabel idSalle;
    private JLabel valueId;
    private JLabel numeroSalle;
    private JLabel typeSalle;
    private JLabel statut;
    private JTextField valueNumero;
    private JTextField valueType;
    private String[] tab = {"Libre", "Réservé", "En maintenance"};
    private JComboBox<String> boxStatut = new JComboBox<>(tab);
    private JButton enregistrer;
    private JButton annuler;
    private final String msgErreurChampVide = "Veillez remplir tous les champs avant d'enregistrer la salle";


    public FrameCreationSalle(Salle salle){

        super("Salle");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(formulaire(salle));
        add(boutons(salle), BorderLayout.SOUTH);

        setVisible(true);
    }

    public JPanel formulaire(Salle salle){
        JPanel panel = new JPanel();
        JLabel label = Fenetre.createLabel("Libre");
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idSalle = Fenetre.createLabel("Identifiant : ");
        numeroSalle = Fenetre.createLabel("Numéro de Salle : ");
        typeSalle = Fenetre.createLabel("Type de Salle : ");
        statut = Fenetre.createLabel("Statut : ");

        if (salle != null){
            valueId = Fenetre.createLabel(String.valueOf(salle.getId()));
            valueNumero = Fenetre.createTextField(salle.getNumeroSalle());
            valueType = Fenetre.createTextField(salle.getTypeSalle());
        }else{
            valueId = Fenetre.createLabel("######");
            valueType = Fenetre.createTextField("");
            valueNumero = Fenetre.createTextField("");
        }
        panel.add(idSalle);
        panel.add(valueId);
        panel.add(numeroSalle);
        panel.add(valueNumero);
        panel.add(typeSalle);
        panel.add(valueType);
        panel.add(statut);
        if (salle != null){
            panel.add(boxStatut);
        }else {
            panel.add(label);
        }

        return panel;
    }

    public JPanel boutons(Salle salle){
        JPanel panel = new JPanel(new FlowLayout());
        enregistrer = new JButton("Enregistrer");
        annuler = new JButton("Annuler");
        enregistrer.setBackground(new Color(72, 255, 0));
        annuler.setBackground(new Color(255, 65, 65));
        panel.add(enregistrer);
        panel.add(annuler);

        enregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String numero = valueNumero.getText();
                String type = valueType.getText();
                String statut = (String) boxStatut.getSelectedItem();

                if (numero == null || type == null || numero.isEmpty() || type.isEmpty() ) {
                    JOptionPane.showMessageDialog(
                            null, msgErreurChampVide, "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }else {

                    Salle s = new Salle(numero, type, statut);

                    if (salle != null){
                        try {
                            salle.setId(Integer.parseInt(valueId.getText()));
                            salle.setNumeroSalle(numero);
                            salle.setTypeSalle(type);
                            salle.setStatut((String) boxStatut.getSelectedItem());
                            salleService.updateSalle(salle);
                            PanelManipulationSalle.chargerSalles();
                            FrameCreationSalle.this.dispose();
                            JOptionPane.showMessageDialog(null, "Mise à jour effectuée.", "Message", JOptionPane.INFORMATION_MESSAGE);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    }else {
                        try {
                            salleService.insertSalle(s);
                            PanelManipulationSalle.chargerSalles();
                            FrameCreationSalle.this.dispose();
                            JOptionPane.showMessageDialog(null, "Salle ajoutée avec succès.", "Message", JOptionPane.INFORMATION_MESSAGE);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        FrameCreationSalle.this.dispose();
                    }
                }


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
