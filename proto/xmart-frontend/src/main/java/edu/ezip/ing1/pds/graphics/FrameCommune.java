package edu.ezip.ing1.pds.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class FrameCommune extends JFrame {

    public FrameCommune() {
        super("Menu principal - ClinicPro");
        setSize(new Dimension(300, 200));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton boutonSecretaire = new JButton("Secrétaire");
        JButton boutonMedecin = new JButton("Médecin");


        JPanel content = (JPanel) getContentPane();
        content.setLayout(new GridLayout(1, 2));
        content.add(boutonMedecin, BorderLayout.NORTH);
        content.add(boutonSecretaire, BorderLayout.SOUTH);


        boutonSecretaire.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Fenetre f = new Fenetre();
                    f.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        boutonMedecin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    FenetreMedecin fm = new FenetreMedecin();
                    fm.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        setVisible(true);
    }


}
