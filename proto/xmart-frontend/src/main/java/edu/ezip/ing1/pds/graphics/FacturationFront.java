package edu.ezip.ing1.pds.graphics;
 
import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.FactureService;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
 
public class FacturationFront {
    private JTextField montantChamp, dateFactureChamp;
    private JCheckBox regleCheckBox;
    private DefaultTableModel model;
    private JTable table;
    private final FactureService factureService;
 
    public FacturationFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.factureService = new FactureService(networkConfig);
 
        JFrame frame = new JFrame("Gestion des Factures");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
 
        JPanel panelNord = new JPanel(new GridLayout(3, 2, 5, 5));
 
        montantChamp = new JTextField();
        dateFactureChamp = new JTextField();
        regleCheckBox = new JCheckBox("Facture réglée");
 
        panelNord.add(new JLabel("Montant :"));
        panelNord.add(montantChamp);
        panelNord.add(new JLabel("Date de facture (yyyy-MM-dd) :"));
        panelNord.add(dateFactureChamp);
        panelNord.add(new JLabel("Réglé :"));
        panelNord.add(regleCheckBox);
 
        frame.add(panelNord, BorderLayout.NORTH);
 
        String[] columns = {"ID", "Date", "Montant", "Réglé"};
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
                
                regleCheckBox.setEnabled(false);
                boolean regle = regleCheckBox.isSelected();
 
                if (montantText.isEmpty() || dateText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Tous les champs doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
 
                double montant = Double.parseDouble(montantText);
                LocalDate date = LocalDate.parse(dateText);
 
                Facture facture = new Facture(date, montant, regle);
                factureService.insertFacture(facture);
 
                chargerFactures();
                viderChamps();
 
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Le montant doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "La date doit être au format AAAA-MM-JJ", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
 
        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                montantChamp.setText(model.getValueAt(i, 2).toString());
                dateFactureChamp.setText(model.getValueAt(i, 1).toString());
                regleCheckBox.setSelected((Boolean) model.getValueAt(i, 3));
            }
        });
 
        boutonModifier.addActionListener(e -> {
            try {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    int id = Integer.parseInt(model.getValueAt(i, 0).toString());
                    double montant = Double.parseDouble(montantChamp.getText());
                    LocalDate date = LocalDate.parse(dateFactureChamp.getText());

                    regleCheckBox.setEnabled(true);
                    boolean regle = regleCheckBox.isSelected();
 
                    Facture facture = new Facture(date, montant, regle);
                    facture.setIdFacture(id);
 
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

         boutonPaiement.addActionListener(e -> {
            // Crée une nouvelle instance de PaiementFront lorsque le bouton est cliqué
            new PaiementFront();
        });
 
        try {
            chargerFactures();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des factures : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
 
        frame.setVisible(true);
    }
 
    private void chargerFactures() throws IOException, InterruptedException {
        model.setRowCount(0);
        Factures factures = factureService.selectFactures();
        if (factures != null && factures.getFactures() != null) {
        for (Facture f : factures.getFactures()) {
            model.addRow(new Object[]{
                f.getIdFacture(),
                f.getDateFacture(),
                f.getMontantFacture(),
                f.getRegle()
            });
        }
    }
}
 
    private void viderChamps() {
        montantChamp.setText("");
        dateFactureChamp.setText("");
        regleCheckBox.setSelected(false);
    }
   
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FacturationFront::new);
    }
}