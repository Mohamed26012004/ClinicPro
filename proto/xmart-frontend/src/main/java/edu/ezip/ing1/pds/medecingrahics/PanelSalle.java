package edu.ezip.ing1.pds.medecingrahics;

import edu.ezip.ing1.pds.business.dto.Salle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelSalle extends JPanel {

    private JLabel labelNumeroSalle;
    private JLabel labelTypeSalle;
    private JLabel labelStutut;

    static PanelSalle panelSalleCliquer;

    public PanelSalle(Salle salle){
        this.labelNumeroSalle = new JLabel(salle.getNumeroSalle());
        this.labelTypeSalle = new JLabel(salle.getTypeSalle());
        this.labelStutut = new JLabel(salle.getStatut());

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                JPanel p = (JPanel) e.getSource();
                if(p != panelSalleCliquer ) setBackground(null);
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
                panelSalleCliquer = (PanelSalle) e.getSource();
            }
        });
        add(labelNumeroSalle);
        add(labelTypeSalle);
        add(labelStutut);
    }

    public Salle salleOfPanel(){
        String numeSalle = this.labelNumeroSalle.getText();
        String typeSalle = this.labelTypeSalle.getText();
        String statut = this.labelStutut.getText();
        Salle salle = new Salle(numeSalle, typeSalle, statut);
        return salle;
    }

    public JLabel getLabelNumeroSalle() {
        return labelNumeroSalle;
    }

    public void setLabelNumeroSalle(JLabel labelNumeroSalle) {
        this.labelNumeroSalle = labelNumeroSalle;
    }

    public JLabel getLabelStutut() {
        return labelStutut;
    }

    public void setLabelStutut(JLabel labelStutut) {
        this.labelStutut = labelStutut;
    }

    public JLabel getLabelTypeSalle() {
        return labelTypeSalle;
    }

    public void setLabelTypeSalle(JLabel labelTypeSalle) {
        this.labelTypeSalle = labelTypeSalle;
    }
}
