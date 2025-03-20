/*/package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.medecingrahics.PanelManipulationExamen;
import edu.ezip.ing1.pds.medecingrahics.PanelManipulationSalle;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


public class Fenetre extends JFrame{


        private JPanel contentPane;
        private static JLabel boutonExamem;
        private static JLabel boutonSalle;
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
            //contentPane.add(new PanelFacture(), BorderLayout.CENTER);

        }
        
            
        public JPanel PanelGauche(){
            JPanel pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

            pane.add(panelBoutonExamen());
            pane.add(Box.createRigidArea(new Dimension(0, 10)));
            pane.add(panelBoutonSalle());
            pane.add(Box.createRigidArea(new Dimension(0, 10)));

            return pane;
        }

        public JPanel panelBoutonExamen(){
            boutonExamem = new JLabel("Examens");
            JPanel panelExamen = new JPanel();
            panelExamen.add(boutonExamem);
            panelExamen.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    contentPane.removeAll();
                    contentPane.add(PanelManipulationExamen.afficheExamens(), BorderLayout.CENTER);
                    contentPane.add(PanelGauche(), BorderLayout.WEST);
                    contentPane.repaint();
                    contentPane.revalidate();
                }
            });
            return panelExamen;
        }

    public JPanel panelBoutonSalle(){
        boutonSalle = new JLabel("Salles");
        JPanel panel = new JPanel();
        panel.add(boutonSalle);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPane.removeAll();
                contentPane.add(PanelManipulationSalle.afficheSalle(), BorderLayout.CENTER);
                contentPane.add(PanelGauche(), BorderLayout.WEST);
                contentPane.repaint();
                contentPane.revalidate();
            }
        });
        return panel;
    }


       
}*/
