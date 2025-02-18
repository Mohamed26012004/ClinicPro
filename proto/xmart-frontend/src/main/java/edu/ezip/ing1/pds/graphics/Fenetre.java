package edu.ezip.ing1.pds.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ExamenService;

public class Fenetre extends JFrame{


        private JPanel contentPane;
        private static JPanel panelGauche;
        private static JButton boutonExamem;
        private static JPanel panelExamen;
        private static JPanel panelCentral;
        
        
        public Fenetre(){
            super("Demo");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
            contentPane = (JPanel)getContentPane();
            contentPane.add(PanelGauche(), BorderLayout.WEST);

            
            try {
                final String networkConfigFile = "network.yaml";
                final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
                final ExamenService examenService = new ExamenService(networkConfig);
                contentPane.add(afficheExamens(examenService.selectExamens()), FlowLayout.CENTER);
            } catch (Exception e) {
            }
           

        }
        
            
        public static JPanel PanelGauche(){
            JPanel pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
            boutonExamem = new JButton("Examens");
            // boutonExamem.addActionListener((e) -> clickBtnExamen(e));
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
