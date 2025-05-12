package edu.ezip.ing1.pds.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DPIFront extends JFrame {

    public DPIFront() {
        super("DPI - ClinicPro");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Icon icon1 = new ImageIcon("C:/Users/Shain1/Documents/SI ING1/SIRIUS/GitHub/ClinicPro/proto/xmart-frontend/src/main/resources/antecedentmedical.png");
        Icon icon2 = new ImageIcon("C:/Users/Shain1/Documents/SI ING1/SIRIUS/GitHub/ClinicPro/proto/xmart-frontend/src/main/resources/symptome.png");
        Icon icon3 = new ImageIcon("C:/Users/Shain1/Documents/SI ING1/SIRIUS/GitHub/ClinicPro/proto/xmart-frontend/src/main/resources/diagnostic.png");
        Icon icon4 = new ImageIcon("C:/Users/Shain1/Documents/SI ING1/SIRIUS/GitHub/ClinicPro/proto/xmart-frontend/src/main/resources/traitement.png");

        JButton bouton1 = new JButton("Antécédent médical", icon1);
        JButton bouton2 = new JButton("Compte-rendu", icon2);
        JButton bouton3 = new JButton("Diagnostic", icon3);
        JButton bouton4 = new JButton("Traitement", icon4);



        // Conteneur principal avec BorderLayout
        setLayout(new BorderLayout());

        // Panel central qui contiendra les boutons
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));
        panelCenter.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel jLabel = new JLabel("Sélectionnez le sous-dossier :");
        jLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCenter.add(jLabel);



        bouton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AntecedentMedicalFront a = new AntecedentMedicalFront();
            }
        });
        bouton1.setMaximumSize(new Dimension(400, 50)); // taille maximale
        bouton1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20))); // espace vertical
        panelCenter.add(bouton1);


        bouton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CompteRenduFront c = new CompteRenduFront();
            }
        });
        bouton2.setMaximumSize(new Dimension(400, 50)); // taille maximale
        bouton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20))); // espace vertical
        panelCenter.add(bouton2);


        bouton3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DiagnosticFront d = new DiagnosticFront();
            }
        });
        bouton3.setMaximumSize(new Dimension(400, 50)); // taille maximale
        bouton3.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20))); // espace vertical
        panelCenter.add(bouton3);



        bouton4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TraitementFront t = new TraitementFront();
            }
        });
        bouton4.setMaximumSize(new Dimension(400, 50)); // taille maximale
        bouton4.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCenter.add(Box.createRigidArea(new Dimension(0, 20))); // espace vertical
        panelCenter.add(bouton4);



        // Ajout du panel au centre de la fenêtre
        add(panelCenter, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DPIFront());
    }
}
