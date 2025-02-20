package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ExamenService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class PanelMaxime extends JPanel {

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final ExamenService examenService = new ExamenService(networkConfig);

    private static JButton ajouter;
    private static JButton modifier;
    private static JButton supprimer;
    private static JScrollPane scrollPane;
    private static JPanel panneau = new JPanel(new BorderLayout());


    public PanelMaxime (){

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(800, 800));
        try {

            ArrayList<PanelExamen> liste = new ArrayList<>();
            for(Examen exam : examenService.selectExamens().getExamens()){
                PanelExamen pan = new PanelExamen(exam);
                liste.add(pan);
            }

            for (int i = 0; i < liste.size(); i++) {
                int a = Integer.parseInt(liste.get(i).getLabelId().getText());
                for (int j=i+1; j<liste.size(); j++){
                    int b = Integer.parseInt(liste.get(j).getLabelId().getText());
                    if(a>b){
                        PanelExamen t = liste.get(i);
                        liste.set(i, liste.get(j));
                        liste.set(j, t);
                        a=b;
                    }
                }
            }
            for(PanelExamen pan : liste){
                add(pan);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static JPanel afficheExamens() {
        PanelMaxime panel = new PanelMaxime();
        scrollPane = new JScrollPane(panel);

        panneau.removeAll();
        panneau.add(scrollPane, BorderLayout.CENTER);

        JPanel titre = new JPanel(new FlowLayout());
        titre.add(new JLabel("TOUS LES EXAMENS : NOM - COÛT - NUMERO DE SALLE"));
        panneau.add(titre, BorderLayout.NORTH);

        panneau.add(boutons(), BorderLayout.SOUTH);

        panneau.revalidate();
        panneau.repaint();

        return panneau;
    }

    public static JPanel boutons(){
        JPanel pane = new JPanel(new FlowLayout());
        ajouter = new JButton("Ajouter");
        ajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FenetreExamen fen = new FenetreExamen(null);
            }
        });
        pane.add(ajouter);

        modifier = new JButton("Modifier");
        modifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Examen examen = new Examen();
                PanelExamen p = PanelExamen.examenSelected;
                p.setBackground(null);
                examen = p.examenDuPanel();
                FenetreExamen fen = new FenetreExamen(examen);
            }
        });
        pane.add(modifier);
        supprimer = new JButton("Supprimer");
        supprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Examen examen = new Examen();
                PanelExamen p = PanelExamen.examenSelected;
                p.setBackground(null);
                examen = p.examenDuPanel();

                final String networkConfigFile = "network.yaml";
                final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
                final ExamenService examenService = new ExamenService(networkConfig);
                try {
                    examenService.deleteExamen(examen);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                PanelMaxime.afficheExamens().revalidate();
                PanelMaxime.afficheExamens().repaint();
            }
        });
        pane.add(supprimer);

        return pane;
    }



}
