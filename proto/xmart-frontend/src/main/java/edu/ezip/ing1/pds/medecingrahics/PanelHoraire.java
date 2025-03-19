package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Horaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PanelHoraire extends JPanel {

    private JLabel labelJour;
    private JLabel labelDebut;
    private JLabel labelFin;
    static PanelHoraire panelHoraireCliquer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    public PanelHoraire(Horaire horaire){
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5 ));
        this.labelJour = new JLabel(horaire.getJour());
        this.labelDebut = new JLabel(horaire.getHeureDebut().format(formatter));
        this.labelFin = new JLabel(horaire.getHeureFin().format(formatter));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        add(labelJour);
        add(labelDebut);
        add(labelFin);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel p = (JPanel) e.getSource();
                p.setBackground(new Color(170, 160, 255));
                panelHoraireCliquer = (PanelHoraire) e.getSource();

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                JPanel p = (JPanel) e.getSource();
                p.setBackground(new Color(233, 230, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                JPanel p = (JPanel) e.getSource();
                if(p != panelHoraireCliquer) p.setBackground(null);
            }
        });

    }
    public Horaire horaireOfPanel(){
        String jour = this.getLabelJour().getText();
        LocalTime heureDebut = LocalTime.parse(this.getLabelDebut().getText(), formatter);
        LocalTime heureFin = LocalTime.parse(this.getLabelFin().getText(), formatter);
        Horaire h = new Horaire(jour, heureDebut, heureFin);
        return h;
    }
    public JLabel getLabelJour() {
        return labelJour;
    }

    public void setLabelJour(JLabel labelJour) {
        this.labelJour = labelJour;
    }

    public JLabel getLabelFin() {
        return labelFin;
    }

    public void setLabelFin(JLabel labelFin) {
        this.labelFin = labelFin;
    }

    public JLabel getLabelDebut() {
        return labelDebut;
    }

    public void setLabelDebut(JLabel labelDebut) {
        this.labelDebut = labelDebut;
    }
}
