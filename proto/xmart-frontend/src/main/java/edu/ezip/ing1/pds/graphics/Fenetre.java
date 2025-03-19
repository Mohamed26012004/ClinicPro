package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.medecingrahics.PanelManipulationSalle;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


public class Fenetre extends JFrame{


        private static JPanel contentPane;
        private static JLabel boutonExamem;
        private static JButton boutonFacture;
        private static JButton boutonPerformance;

        
        public Fenetre(){
            super("Demo");
            setSize(700, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
            contentPane = (JPanel)getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(PanelGauche(), BorderLayout.WEST);

        }
        
            
        public static JPanel PanelGauche(){
            JPanel pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

            pane.add(panelBoutonExamen());
            pane.add(Box.createRigidArea(new Dimension(0, 5)));
                
            return pane;
        }

        public static JPanel panelBoutonExamen(){
            boutonExamem = new JLabel("Salles");
            JPanel panelExamen = new JPanel();
            panelExamen.add(boutonExamem);
            panelExamen.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    contentPane.removeAll();
                    contentPane.add(new PanelManipulationSalle(), BorderLayout.CENTER);
                    contentPane.add(Fenetre.PanelGauche(), BorderLayout.WEST);
                    contentPane.repaint();
                    contentPane.revalidate();
                }
            });
            return panelExamen;
        }


        

       
}
