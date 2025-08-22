package edu.ezip.ing1.pds.graphics.medecin;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.examen.FrameDeSelectionExamen;
import edu.ezip.ing1.pds.graphics.examen.PanelExamen;
import edu.ezip.ing1.pds.graphics.rendezvous.PanelRendezVous;
import edu.ezip.ing1.pds.graphics.salle.FrameDeSelectionSalle;
import edu.ezip.ing1.pds.services.planning.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.*;

public class FrameInsertUpdatePlanification extends JFrame {

    final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final PlanificationService planificationService = new PlanificationService(networkConfig);
    static final PatientService patientService = new PatientService(networkConfig);
    static final SalleService salleService = new SalleService(networkConfig);
    static final ExamenService examenService = new ExamenService(networkConfig);
    static final MedecinService medecinService = new MedecinService(networkConfig);

    private static PlanificationExamen planificationToInsert;
    private static JPanel contentPane;
    public static DefaultTableModel modelPatient;
    public static JTable tablePatient;
    public static DefaultTableModel modelExamen;
    public static JTable tableExamen;
    public static DefaultTableModel modelSalle;
    public static JTable tableSalle;
    public static DefaultTableModel modelMedecin;
    public static JTable tableMedecin;
    public static int idPreventSalle;
    protected static int choix;

    public FrameInsertUpdatePlanification(PlanificationExamen planificationExamen, int k) {

        super("Programmer un examen");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        planificationToInsert = planificationExamen;
        idPreventSalle = planificationExamen.getIdSalle();
        this.choix = k;
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        if (this.choix == 1 || this.choix == 3) {
            contentPane.add(panelMedecin());
            contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
            setSize(600, 650);
            setLocationRelativeTo(null);
        }
        contentPane.add(panelPatient());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        contentPane.add(panelExamen());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        contentPane.add(panelSalle());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        contentPane.add(southButton());
        try {
            modification(planificationExamen, k);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setVisible(true);
    }

    public static JPanel panelPatient() {
        JPanel panel = new JPanel();
        Border titre = BorderFactory.createTitledBorder("PATIENT");
        Border espaceVide = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(BorderFactory.createCompoundBorder(titre, espaceVide));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton selectionner = new JButton("Sélectionner");
        selectionner.setFont(new Font("Arial", Font.PLAIN, 16));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameDeSelectionPatient f = new FrameDeSelectionPatient();
            }
        });
        selectionner.setBackground(new Color(115, 91, 255));
        panelBouton.add(selectionner);
        panel.add(panelBouton);

        String[] columns = {"ID", "Nom", "Prénom", "Téléphone", "Adresse"};
        modelPatient = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePatient = new JTable(modelPatient);
        tablePatient.setRowHeight(30);
        tablePatient.setFont(new Font("Arial", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(tablePatient);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel panelExamen() {
        JPanel panel = new JPanel();

        Border titre = BorderFactory.createTitledBorder("EXAMEN");
        Border espaceVide = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(BorderFactory.createCompoundBorder(titre, espaceVide));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton selectionner = new JButton("Sélectionner");
        selectionner.setFont(new Font("Arial", Font.PLAIN, 16));
        selectionner.setBackground(new Color(115, 91, 255));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameDeSelectionExamen f = new FrameDeSelectionExamen();
            }
        });
        panelBouton.add(selectionner);
        panel.add(panelBouton);

        String[] columns = {"ID", "Nom", "Coût", "Durée"};
        modelExamen = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        tableExamen = new JTable(modelExamen);
        tableExamen.setRowHeight(30);
        tableExamen.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(tableExamen);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel panelSalle() {
        JPanel panel = new JPanel();

        Border titre = BorderFactory.createTitledBorder("SALLE");
        Border espaceVide = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(BorderFactory.createCompoundBorder(titre, espaceVide));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton selectionner = new JButton("Sélectionner");
        selectionner.setFont(new Font("Arial", Font.PLAIN, 16));
        selectionner.setBackground(new Color(115, 91, 255));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameDeSelectionSalle f = new FrameDeSelectionSalle();
            }
        });
        panelBouton.add(selectionner);
        panel.add(panelBouton);

        String[] columns = {"ID", "Numero de Salle", "Type", "Statut"};
        modelSalle = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSalle = new JTable(modelSalle);
        tableSalle.setRowHeight(30);
        tableSalle.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(tableSalle);

        panel.add(scrollPane);

        return panel;
    }

    public JPanel southButton() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton enregistrer = new JButton("Enregistrer");
        JButton annuler = new JButton("Annuler");

        enregistrer.setBackground(new Color(72, 255, 0));
        enregistrer.setFont(new Font("Arial", Font.PLAIN, 16));
        annuler.setBackground(new Color(255, 65, 65));
        annuler.setFont(new Font("Arial", Font.PLAIN, 16));

        annuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameInsertUpdatePlanification.this.dispose();
            }
        });
        enregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                tableExamen.setRowSelectionInterval(0, 0);
                tableSalle.setRowSelectionInterval(0, 0);
                tablePatient.setRowSelectionInterval(0, 0);
                if (choix == 1 || choix == 3) {
                    tableMedecin.setRowSelectionInterval(0, 0);
                    int i = tableExamen.getSelectedRow();
                    int j = tablePatient.getSelectedRow();
                    int k = tableSalle.getSelectedRow();
                    int t = tableMedecin.getSelectedRow();

                    if (i >= 0 && j >= 0 && k >= 0 && t >= 0) {
                        getPlanificationToInsert().setNumeroADELI(Integer.parseInt(modelMedecin.getValueAt(t, 0).toString()));
                        getPlanificationToInsert().setIdExamen(Integer.parseInt(modelExamen.getValueAt(i, 0).toString()));
                        getPlanificationToInsert().setIdPatient(Integer.parseInt(modelPatient.getValueAt(j, 0).toString()));
                        int idUpdate = Integer.parseInt(modelSalle.getValueAt(k, 0).toString());
                        getPlanificationToInsert().setIdSalle(idUpdate);
                        try {
                            if (choix == 1) {
                                planificationService.insertPlanification(getPlanificationToInsert());
                                JOptionPane.showMessageDialog(null, "Créneau Réservé.", "Information", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                if (idPreventSalle != idUpdate ){
                                    Salle salle = new Salle();
                                    salle.setId(idPreventSalle);
                                    salleService.updateSalle(salle);
                                }
                                planificationService.updatePlanification(getPlanificationToInsert());
                                JOptionPane.showMessageDialog(null, "Mis à jour éffectuée.", "Information", JOptionPane.INFORMATION_MESSAGE);
                            }

                            dispose();
                            PanelRendezVous.chargerDisponibilite(PanelRendezVous.planificationDuMedecin);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Merci de renseigner tous les éléments demandés", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    int i = tableExamen.getSelectedRow();
                    int j = tablePatient.getSelectedRow();
                    int k = tableSalle.getSelectedRow();

                    if (i >= 0 && j >= 0 && k >= 0) {
                        getPlanificationToInsert().setIdExamen(Integer.parseInt(modelExamen.getValueAt(i, 0).toString()));
                        getPlanificationToInsert().setIdPatient(Integer.parseInt(modelPatient.getValueAt(j, 0).toString()));
                        int idUpdate = Integer.parseInt(modelSalle.getValueAt(k, 0).toString());
                        getPlanificationToInsert().setIdSalle(idUpdate);
                        try {
                            if (choix == 0) {
                                planificationService.insertPlanification(getPlanificationToInsert());
                                JOptionPane.showMessageDialog(null, "Créneau Réservé", "Information", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                if (idPreventSalle != idUpdate ){
                                    Salle salle = new Salle();
                                    salle.setId(idPreventSalle);
                                    salleService.updateSalle(salle);
                                }
                                planificationService.updatePlanification(getPlanificationToInsert());
                                JOptionPane.showMessageDialog(null, "Réservation Mis à jour", "Information", JOptionPane.INFORMATION_MESSAGE);
                            }
                            dispose();
                            PanelPlanningMedecin.chargerDisponibilite(PanelPlanningMedecin.planificationDuMedecin);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Merci de renseigner tous les éléments demandés", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        panel.add(enregistrer);
        panel.add(annuler);
        return panel;
    }

    public static JPanel panelMedecin() {
        JPanel panel = new JPanel();

        Border titre = BorderFactory.createTitledBorder("MEDECIN");
        Border espaceVide = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(BorderFactory.createCompoundBorder(titre, espaceVide));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton selectionner = new JButton("Sélectionner");
        selectionner.setFont(new Font("Arial", Font.PLAIN, 16));
        selectionner.setBackground(new Color(115, 91, 255));
        selectionner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameDeSelectionMedecin f = new FrameDeSelectionMedecin(planificationToInsert);
            }
        });
        panelBouton.add(selectionner);
        panel.add(panelBouton);

        String[] columns = {"Numéro ADELI", "Nom", "Prénom", "Téléphone", "Spécialité", "Salaire"};
        modelMedecin = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMedecin = new JTable(modelMedecin);
        tableMedecin.setRowHeight(30);
        tableMedecin.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(tableMedecin);

        panel.add(scrollPane);

        return panel;
    }


    public PlanificationExamen getPlanificationToInsert() {
        return planificationToInsert;
    }

    public static void modification(PlanificationExamen planificationExamen, int k) throws ExecutionException, InterruptedException, IOException {

        if (k == 3) {
            Medecin medecin = new Medecin();
            Patient patient = new Patient();
            Examen examen = new Examen();
            Salle salle = new Salle();

            Medecins medecins = medecinService.selectAllMedecins();
            for (Medecin m : medecins.getMedecins()) {
                if (m.getNumeroADELI() == planificationExamen.getNumeroADELI()) {
                    medecin = m;
                    break;
                }
            }


            Patients patients = patientService.selectPatients();
            for (Patient p : patients.getPatients()) {
                if (p.getIdPatient() == planificationExamen.getIdPatient()) {
                    patient = p;
                    break;
                }
            }

            Examens examens = examenService.selectExamens();
            for (Examen e : examens.getExamens()) {
                if (e.getId() == planificationExamen.getIdExamen()) {
                    examen = e;
                    break;
                }
            }

            Salles salles = salleService.selectSalles();
            for (Salle s : salles.getSalles()) {
                if (s.getId() == planificationExamen.getIdSalle()) {
                    salle = s;
                    break;
                }
            }

            modelMedecin.addRow(new Object[]{
                    medecin.getNumeroADELI(),
                    medecin.getNom(),
                    medecin.getPrenom(),
                    medecin.getTelephone(),
                    medecin.getSpecialite(),
                    medecin.getSalaire()
            });
            tableMedecin.setRowSelectionInterval(0, 0);

            modelExamen.addRow(new Object[]{
                    examen.getId(),
                    examen.getNom(),
                    examen.getCout(),
                    examen.getDuree(),
            });
            tableExamen.setRowSelectionInterval(0, 0);
            modelSalle.addRow(new Object[]{
                    salle.getId(),
                    salle.getNumeroSalle(),
                    salle.getTypeSalle(),
                    salle.getStatut()
            });
            tableSalle.setRowSelectionInterval(0, 0);
            modelPatient.addRow(new Object[]{
                    patient.getIdPatient(),
                    patient.getNom(),
                    patient.getPrenom(),
                    patient.getTelephone(),
                    patient.getAdresse()
            });
            tablePatient.setRowSelectionInterval(0, 0);

        } else if (k == 2) {

            Patient patient = new Patient();
            Examen examen = new Examen();
            Salle salle = new Salle();

            Patients patients = patientService.selectPatients();
            for (Patient p : patients.getPatients()) {
                if (p.getIdPatient() == planificationExamen.getIdPatient()) {
                    patient = p;
                    break;
                }

                Examens examens = examenService.selectExamens();
                for (Examen e : examens.getExamens()) {
                    if (e.getId() == planificationExamen.getIdExamen()) {
                        examen = e;
                        break;
                    }
                }

                Salles salles = salleService.selectSalles();
                for (Salle s : salles.getSalles()) {
                    if (s.getId() == planificationExamen.getIdSalle()) {
                        salle = s;
                        break;
                    }
                }

                modelExamen.addRow(new Object[]{
                        examen.getId(),
                        examen.getNom(),
                        examen.getCout(),
                        examen.getDuree(),
                });
                tableExamen.setRowSelectionInterval(0, 0);

                modelSalle.addRow(new Object[]{
                        salle.getId(),
                        salle.getNumeroSalle(),
                        salle.getTypeSalle(),
                        salle.getStatut()
                });
                tableExamen.setRowSelectionInterval(0, 0);
                modelPatient.addRow(new Object[]{
                        patient.getIdPatient(),
                        patient.getNom(),
                        patient.getPrenom(),
                        patient.getTelephone(),
                        patient.getAdresse()
                });
                tablePatient.setRowSelectionInterval(0, 0);
            }

        }
    }
}