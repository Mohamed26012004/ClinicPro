package edu.ezip.ing1.pds.graphics.medecin;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
import edu.ezip.ing1.pds.graphics.Fenetre;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PanelPlanningMedecin extends JPanel {

    private JSplitPane splitPane;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public PanelPlanningMedecin(){
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel(), rightPanel());
        add(splitPane);
    }


    public static JPanel leftPanel(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel();
        // Planning ou disponiblité
        JRadioButton planningButton = new JRadioButton("Planning");
        JRadioButton disponibiliteButton = new JRadioButton("Disponibilité");

        ButtonGroup group = new ButtonGroup();
        group.add(planningButton);
        group.add(disponibiliteButton);

        panel1.add(planningButton);
        panel1.add(disponibiliteButton);
        panel.add(panel1);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel2 = new JPanel(new BorderLayout());

        return panel;
    }

    public static JPanel rightPanel(){
        JPanel panel = new JPanel();


        return panel;
    }
}
