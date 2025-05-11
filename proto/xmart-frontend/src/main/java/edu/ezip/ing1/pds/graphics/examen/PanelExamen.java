package edu.ezip.ing1.pds.graphics.examen;

import edu.ezip.ing1.pds.business.dto.Examen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PanelExamen extends JPanel {

    private JLabel labelNom;
    private JLabel labelCout;
    private JLabel labelDuree;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    static PanelExamen panelExamenCliquer;

    public PanelExamen (Examen examen){
        this.labelNom = new JLabel(examen.getNom());
        this.labelCout = new JLabel(String.valueOf(examen.getCout()));
        this.labelDuree = new JLabel(examen.getDuree().format(formatter));

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                JPanel p = (JPanel) e.getSource();
                if(p != panelExamenCliquer ) setBackground(null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                JPanel p = (JPanel) e.getSource();
                p.setBackground(new Color(233, 230, 255));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel p = (JPanel) e.getSource();
                p.setBackground(new Color(170, 160, 255));
                panelExamenCliquer = (PanelExamen) e.getSource();
            }
        });
        add(labelNom);
        add(labelCout);
        add(labelDuree);
    }

    public Examen ExamenOfPanel(){
        String nom = this.getLabelNom().getText();
        double cout = Double.parseDouble(this.labelCout.getText());
        LocalTime duree = LocalTime.parse(this.labelDuree.getText(), formatter);
        Examen examen = new Examen(nom, cout, duree);
        return examen;
    }

    public JLabel getLabelCout() {
        return labelCout;
    }

    public void setLabelCout(JLabel labelCout) {
        this.labelCout = labelCout;
    }

    public JLabel getLabelDuree() {
        return labelDuree;
    }

    public void setLabelDuree(JLabel labelDuree) {
        this.labelDuree = labelDuree;
    }

    public JLabel getLabelNom() {
        return labelNom;
    }

    public void setLabelNom(JLabel labelNom) {
        this.labelNom = labelNom;
    }
}
