package edu.ezip.ing1.pds.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;

public class Fenetre extends JFrame{


        private static JPanel contentPane;
        private static JPanel panelGauche;
        private static JButton boutonExamem;
        private static JPanel panelExamen;
        private static JPanel panelCentral;
        //private static JScrollPane scrollPane = new JScrollPane();
        
        public Fenetre(){
            super("Demo");
            setSize(700, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
            contentPane = (JPanel)getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(PanelGauche(), BorderLayout.WEST);
            //JPanel panel = (JPanel) new PanelMaxime();
            contentPane.add(PanelMaxime.afficheExamens(), BorderLayout.CENTER);

//            try {
//                final String networkConfigFile = "network.yaml";
//                final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
//                final ExamenService examenService = new ExamenService(networkConfig);
//                contentPane.add(afficheExamens(examenService.selectExamens()), FlowLayout.CENTER);
//            } catch (Exception e) {
//            }
//

        }
        
            
        public static JPanel PanelGauche(){
            JPanel pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
            boutonExamem = new JButton("Examens");
            boutonExamem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    contentPane.add(PanelMaxime.afficheExamens(), BorderLayout.CENTER);
                }
            });
            pane.add(boutonExamem);
            pane.add(Box.createRigidArea(new Dimension(0, 5)));
                
                
            return pane;
        }
        
        public static JPanel afficheExamens(Examens examens){
        
        
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            for (Examen examen : examens.getExamens()) {
                JPanel pane = new JPanel();
                pane.setLayout(new FlowLayout());
                JLabel nom = new JLabel(examen.getNom());
                JLabel cout = new JLabel(String.valueOf(examen.getCout()));
                JLabel numSalle = new JLabel(examen.getNumeroSalle());
                JLabel id = new JLabel(String.valueOf(examen.getId()));
                pane.add(id);
                pane.add(nom);
                pane.add(cout);
                pane.add(numSalle);

                panel.add(pane);
            }

            return panel;
        }

        

       
}
