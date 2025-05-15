package edu.ezip.ing1.pds.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class FrameCommune extends JFrame {

    public FrameCommune() {
        super("Menu principal - ClinicPro");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton bouton1 = new JButton("Secrétaire");
        JButton bouton2 = new JButton("Médecin");

        setLayout(new BorderLayout());

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));
        panelCenter.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel jLabel = new JLabel("Sélectionnez votre interface");
        jLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCenter.add(jLabel);


        bouton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Fenetre f = new Fenetre();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        bouton1.setMaximumSize(new Dimension(400, 50)); // taille maximale
        bouton1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20))); // espace vertical
        panelCenter.add(bouton1);


        bouton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    FenetreMedecin fm = new FenetreMedecin();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        bouton2.setMaximumSize(new Dimension(400, 50)); // taille maximale
        bouton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20))); // espace vertical
        panelCenter.add(bouton2);


        add(panelCenter, BorderLayout.CENTER);

        setVisible(true);
    }

}
