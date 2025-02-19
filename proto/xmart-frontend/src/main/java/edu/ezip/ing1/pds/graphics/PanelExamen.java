package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ExamenService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PanelExamen extends JPanel {
    private JLabel labelId;
    private JLabel labelNom;
    private JLabel labelCout;
    private JLabel labelNumeroSalle;

    final String networkConfigFile = "network.yaml";
    final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final ExamenService examenService = new ExamenService(networkConfig);
    protected static PanelExamen examenSelected;

    public PanelExamen(Examen examen) throws IOException, InterruptedException{
        setLayout(new FlowLayout(FlowLayout.LEFT, 8,8));
        Examen exam = examenService.selectOneExamen(examen);
        this.labelId = new JLabel(String.valueOf(exam.getId()));
        this.labelNom = new JLabel(exam.getNom());
        this.labelCout = new JLabel(String.valueOf(exam.getCout()));
        this.labelNumeroSalle = new JLabel(exam.getNumeroSalle());
        add(labelId);
        add(labelNom);
        add(labelCout);
        add(labelNumeroSalle);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                PanelExamen pane;
                pane = (PanelExamen)e.getSource();
                pane.setBackground(new Color(150, 150, 150));
                examenSelected = pane;

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(214, 214, 214));
            }

            @Override
            public void mouseExited(MouseEvent e2) {
                PanelExamen pane = (PanelExamen)e2.getSource();
                if(pane != examenSelected ) setBackground(null);
            }
        });
    }
    public Examen examenDuPanel(){

        String nom = this.getLabelNom().getText();
        double cout = Double.parseDouble(this.getLabelCout().getText());
        String num = this.getLabelNumeroSalle().getText();

        return new Examen(nom, cout, num);
    }
    public JLabel getLabelCout() {
        return labelCout;
    }

    public void setLabelCout(JLabel labelCout) {
        this.labelCout = labelCout;
    }

    public JLabel getLabelId() {
        return labelId;
    }

    public void setLabelId(JLabel labelId) {
        this.labelId = labelId;
    }

    public JLabel getLabelNom() {
        return labelNom;
    }

    public void setLabelNom(JLabel labelNom) {
        this.labelNom = labelNom;
    }

    public JLabel getLabelNumeroSalle() {
        return labelNumeroSalle;
    }

    public void setLabelNumeroSalle(JLabel labelNumeroSalle) {
        this.labelNumeroSalle = labelNumeroSalle;
    }
}
