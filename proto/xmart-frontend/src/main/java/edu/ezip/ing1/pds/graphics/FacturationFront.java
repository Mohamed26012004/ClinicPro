package edu.ezip.ing1.pds.graphics;

import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ExamenService;
import edu.ezip.ing1.pds.services.FactureService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class FacturationFront {

    private JTextField montantChamp, dateFactureChamp;
    private JCheckBox regleCheckBox;
    private JComboBox<String> examenCombobox;
    private DefaultTableModel model;
    private JTable table;
    private final FactureService factureService;
    private ArrayList<Examen> examensListe;

    public FacturationFront() throws InterruptedException, IOException {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final ExamenService examenService = new ExamenService(networkConfig);
        this.factureService = new FactureService(networkConfig);

        JFrame frame = new JFrame("Gestion des Factures");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panelNord = new JPanel(new GridLayout(4, 2, 5, 5));

        examensListe = new ArrayList<>();
        Examens examens = examenService.selectExamens();
        if (examens != null && examens.getExamens() != null) {
            examensListe = new ArrayList<>(examens.getExamens());
            String[] noms = new String[examensListe.size()];
            for (int i = 0; i < noms.length; i++) {
                noms[i] = examensListe.get(i).getNom();
            }
            examenCombobox = new JComboBox<>(noms);
        }

        montantChamp = new JTextField();
        dateFactureChamp = new JTextField();
        regleCheckBox = new JCheckBox("Facture réglée");
        regleCheckBox.setEnabled(false);

        panelNord.add(new JLabel("Examen :"));
        panelNord.add(examenCombobox);
        panelNord.add(new JLabel("Montant :"));
        panelNord.add(montantChamp);
        panelNord.add(new JLabel("Date (AAAA-MM-JJ) :"));
        panelNord.add(dateFactureChamp);
        panelNord.add(new JLabel("Réglé :"));
        panelNord.add(regleCheckBox);

        frame.add(panelNord, BorderLayout.NORTH);

        String[] columns = {"ID", "Date", "Montant", "Réglé", "Examen"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelSud = new JPanel();
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");
        JButton boutonPaiement = new JButton("Paiement");

        panelSud.add(boutonAjouter);
        panelSud.add(boutonModifier);
        panelSud.add(boutonSupprimer);
        panelSud.add(boutonPaiement);

        frame.add(panelSud, BorderLayout.SOUTH);

        boutonAjouter.addActionListener(e -> {
            try {
                String montantText = montantChamp.getText();
                String dateText = dateFactureChamp.getText();
                boolean regle = regleCheckBox.isSelected();

                if (montantText.isEmpty() || dateText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Tous les champs doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double montant = Double.parseDouble(montantText);
                LocalDate date = LocalDate.parse(dateText);

                int selectedIndex = examenCombobox.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un examen", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idExamen = examensListe.get(selectedIndex).getId();
                Facture facture = new Facture(date, montant, regle);
                facture.setIdExamen(idExamen);
                factureService.insertFacture(facture);

                chargerFactures();
                viderChamps();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Le montant doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Date invalide (format AAAA-MM-JJ)", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                montantChamp.setText(model.getValueAt(i, 2).toString());
                dateFactureChamp.setText(model.getValueAt(i, 1).toString());
                regleCheckBox.setSelected((Boolean) model.getValueAt(i, 3));
                regleCheckBox.setEnabled(true); // revoir si s'active bien lors de selection de ligne

                String nomExamen = model.getValueAt(i, 4).toString();
                for (int j = 0; j < examenCombobox.getItemCount(); j++) {
                    if (examenCombobox.getItemAt(j).equals(nomExamen)) {
                        examenCombobox.setSelectedIndex(j);
                        break;
                    }
                }
            }
        });

        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    int id = Integer.parseInt(model.getValueAt(i, 0).toString());
                    double montant = Double.parseDouble(montantChamp.getText());
                    LocalDate date = LocalDate.parse(dateFactureChamp.getText());
                    boolean regle = regleCheckBox.isSelected();
                    int idExamen = examensListe.get(examenCombobox.getSelectedIndex()).getId();

                    Facture facture = new Facture(date, montant, regle);
                    facture.setIdFacture(id);
                    facture.setIdExamen(idExamen);
                    factureService.updateFacture(facture);

                    chargerFactures();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonSupprimer.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    Facture facture = new Facture();
                    facture.setIdFacture(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    factureService.deleteFacture(facture);
                    chargerFactures();
                    viderChamps();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonPaiement.addActionListener(e -> new PaiementFront());

        chargerFactures();
        frame.setVisible(true);
    }

    private void chargerFactures() throws IOException, InterruptedException {
        model.setRowCount(0);
        Factures factures = factureService.selectFactures();
        if (factures != null && factures.getFactures() != null) {
            for (Facture f : factures.getFactures()) {
                String nomExamen = "";
                for (Examen e : examensListe) {
                    if (e.getId() == f.getIdExamen()) {
                        nomExamen = e.getNom();
                        break;
                    }
                }

                model.addRow(new Object[]{
                        f.getIdFacture(),
                        f.getDateFacture(),
                        f.getMontantFacture(),
                        f.getRegle(),
                        nomExamen
                });
            }
        }
    }

    private void viderChamps() {
        montantChamp.setText("");
        dateFactureChamp.setText("");
        regleCheckBox.setSelected(false);
        regleCheckBox.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new FacturationFront();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
