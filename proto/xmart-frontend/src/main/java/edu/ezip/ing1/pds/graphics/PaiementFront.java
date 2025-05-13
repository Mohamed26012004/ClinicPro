package edu.ezip.ing1.pds.graphics;
 
import edu.ezip.ing1.pds.business.dto.Paiement;
import edu.ezip.ing1.pds.business.dto.Paiements;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.PaiementService;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
 
public class PaiementFront {
    private JTextField montantChamp, datePaiementChamp;
    private JRadioButton cartebancaireRadio, especesRadio, chequeRadio, tierspayantRadio;
    private ButtonGroup moyenDePaiementGroup; //https://koor.fr/Java/TutorialSwing/swing_JRadioButton.wp //Unique bouton coché
    private DefaultTableModel model;
    private JTable table;
    private final PaiementService paiementService;
 
    public PaiementFront() {
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        this.paiementService = new PaiementService(networkConfig);
 
        JFrame frame = new JFrame("Gestion des Paiements");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
 
        JPanel panelNord = new JPanel(new GridLayout(3, 2, 5, 5));
 
        montantChamp = new JTextField();
        datePaiementChamp = new JTextField();
       
        cartebancaireRadio = new JRadioButton("Carte Bancaire");
        especesRadio = new JRadioButton("Espèces");
        chequeRadio = new JRadioButton("Chèque");
        tierspayantRadio = new JRadioButton("Tiers-Payant");
 
        moyenDePaiementGroup = new ButtonGroup();
        moyenDePaiementGroup.add(cartebancaireRadio);
        moyenDePaiementGroup.add(especesRadio);
        moyenDePaiementGroup.add(chequeRadio);
        moyenDePaiementGroup.add(tierspayantRadio);
 
        panelNord.add(new JLabel("Montant :"));
        panelNord.add(montantChamp);
        panelNord.add(new JLabel("Date de paiement (yyyy-MM-dd) :"));
        panelNord.add(datePaiementChamp);
        panelNord.add(new JLabel("Moyen de Paiement :"));
 
        JPanel panelRadio = new JPanel(new GridLayout(2, 2));
        panelRadio.add(cartebancaireRadio);
        panelRadio.add(especesRadio);
        panelRadio.add(chequeRadio);
        panelRadio.add(tierspayantRadio);
        panelNord.add(panelRadio);
 
        frame.add(panelNord, BorderLayout.NORTH);
 
        String[] columns = {"ID", "Montant", "Date de paiement", "Moyen de Paiement"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
 
        JPanel panelSud = new JPanel();
        JButton boutonSoumettre = new JButton("Soumettre");
        JButton boutonAnnuler = new JButton("Annuler");
 
        panelSud.add(boutonSoumettre);
        panelSud.add(boutonAnnuler);
 
        frame.add(panelSud, BorderLayout.SOUTH);
 
        boutonSoumettre.addActionListener(e -> {
            try {
                String montantText = montantChamp.getText();
                String dateText = datePaiementChamp.getText();
               
                String moyen = "";
                if (cartebancaireRadio.isSelected()) {
                    moyen = "Carte Bancaire";
                } else if (especesRadio.isSelected()) {
                    moyen = "Espèces";
                } else if (chequeRadio.isSelected()) {
                    moyen = "Chèque";
                } else if (tierspayantRadio.isSelected()) {
                    moyen = "Tiers-Payant";
                }
 
                if (montantText.isEmpty() && dateText.isEmpty() && moyen.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Tous les champs doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
 
                double montant = Double.parseDouble(montantText);
 
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate datePaiement = LocalDate.parse(dateText, formatter);
 
                Paiement paiement = new Paiement();
                paiement.setmontant(montant);
                paiement.setdatePaiement(datePaiement);
                paiement.setmoyenDePaiement(moyen);
 
                paiementService.insertPaiement(paiement);
                chargerPaiements();
                viderChamps();
 
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Le montant doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Format de date invalide. Utilisez le format yyyy-MM-dd", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
 
        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                montantChamp.setText(model.getValueAt(i, 1).toString());
                datePaiementChamp.setText(model.getValueAt(i, 2).toString());
            }
        });
 
        boutonAnnuler.addActionListener(e -> frame.dispose());
 
        try {
            chargerPaiements();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des paiements: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
 
        frame.setVisible(true);
    }
 
    private void chargerPaiements() throws IOException, InterruptedException {
        model.setRowCount(0);
        Paiements paiements = paiementService.selectPaiements();
        if (paiements != null && paiements.getPaiements() != null) {
            for (Paiement p : paiements.getPaiements()) {
                model.addRow(new Object[]{
                    p.getidPaiement(),
                    p.getmontant(),
                    p.getdatePaiement(),
                    p.getmoyenDePaiement()
                });
            }
        }
    }
 
    private void viderChamps() {
        montantChamp.setText("");
        datePaiementChamp.setText("");
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaiementFront::new);
    }
}