package edu.ezip.ing1.pds.medecingrahics;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class PanelCreneau extends JPanel {


    private JLabel labelDebut;
    private JLabel labelFin;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public PanelCreneau (Creneau creneau){
        this.labelDebut = new JLabel(creneau.getDebut().format(formatter));
        this.labelFin = new JLabel(creneau.getFin().format(formatter));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(labelDebut);
        add(labelFin);
    }

    public JLabel getLabelDebut() {
        return labelDebut;
    }

    public void setLabelDebut(JLabel labelDebut) {
        this.labelDebut = labelDebut;
    }

    public JLabel getLabelFin() {
        return labelFin;
    }

    public void setLabelFin(JLabel labelFin) {
        this.labelFin = labelFin;
    }
}
