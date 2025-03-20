package edu.ezip.ing1.pds.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class Fenetre extends JFrame{


        private static JPanel contentPane;
        private static JButton boutonExamem;
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
            //JPanel panel = (JPanel) new PanelMaxime();
           // contentPane.add(PanelMaxime.afficheExamens(), BorderLayout.CENTER);
            //contentPane.add(new PanelFacture(), BorderLayout.CENTER);

        }
        
            
        public static JPanel PanelGauche(){
            JPanel pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
            boutonExamem = new JButton("Examens");
            boutonExamem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    contentPane.removeAll();
                    //contentPane.add(PanelMaxime.afficheExamens(), BorderLayout.CENTER);
                    contentPane.add(Fenetre.PanelGauche(), BorderLayout.WEST);
                    contentPane.repaint();
                    contentPane.revalidate();
                }
            });
            pane.add(boutonExamem);
            pane.add(Box.createRigidArea(new Dimension(0, 5)));

            boutonFacture = new JButton("Facture");
            boutonFacture.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PanelFacture panel = new PanelFacture();
                    contentPane.removeAll();
                    contentPane.add(panel, BorderLayout.CENTER);
                    contentPane.add(Fenetre.PanelGauche(), BorderLayout.WEST);
                    contentPane.repaint();
                    contentPane.revalidate();
                }
            });
            pane.add(boutonFacture);
            pane.add(Box.createRigidArea(new Dimension(0, 5)));

            boutonPerformance = new JButton("Performance");
            boutonPerformance.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PanelPerformance panel = new PanelPerformance();                   contentPane.removeAll();
                    contentPane.add(panel, BorderLayout.CENTER);
                    contentPane.add(Fenetre.PanelGauche(), BorderLayout.WEST);
                    contentPane.repaint();
                    contentPane.revalidate();
                }
            });
            pane.add(boutonPerformance);
                
            return pane;
        }


        

       
}
