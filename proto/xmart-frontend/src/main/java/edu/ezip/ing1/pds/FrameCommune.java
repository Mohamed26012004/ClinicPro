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

        JButton boutonSecretaire = new JButton("Secrétaire");
        JButton boutonMedecin = new JButton("Médecin");

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


        boutonSecretaire.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Fenetre f = new Fenetre();
                    f.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        boutonSecretaire.setMaximumSize(new Dimension(400, 50));
        boutonSecretaire.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCenter.add(boutonSecretaire);


        boutonMedecin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    FenetreMedecin fm = new FenetreMedecin();
                    fm.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        boutonMedecin.setMaximumSize(new Dimension(400, 50));
        boutonMedecin.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCenter.add(boutonMedecin);


        add(panelCenter, BorderLayout.CENTER);

        setVisible(true);
    }


}
