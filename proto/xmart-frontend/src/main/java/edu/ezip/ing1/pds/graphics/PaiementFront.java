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
import java.util.ArrayList;
import java.util.Comparator;
 
public class PaiementFront extends JPanel {
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
 
        setLayout(new BorderLayout());
        setSize(700, 400);
 
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
        panelNord.add(new JLabel("Date de paiement (AAAA-MM-DD) :"));
        panelNord.add(datePaiementChamp);
        panelNord.add(new JLabel("Moyen de Paiement :"));
 
        JPanel panelRadio = new JPanel(new GridLayout(2, 2));
        panelRadio.add(cartebancaireRadio);
        panelRadio.add(especesRadio);
        panelRadio.add(chequeRadio);
        panelRadio.add(tierspayantRadio);
        panelNord.add(panelRadio);
 
        add(panelNord, BorderLayout.NORTH);
 
        String[] columns = {"ID", "Montant", "Date de paiement", "Moyen de Paiement"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
 
        JPanel panelSud = new JPanel();
        JButton boutonSoumettre = new JButton("Soumettre");
 
        panelSud.add(boutonSoumettre);
 
        add(panelSud, BorderLayout.SOUTH);
 
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
                    JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                if (moyen.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vous devez sélectionner un moyen de paiement", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
 
                double montant = Double.parseDouble(montantText);
 
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate datePaiement = LocalDate.parse(dateText, formatter);

                int confirmation = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir confirmer ce paiement ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                
                if (confirmation == JOptionPane.YES_OPTION) {

                Paiement paiement = new Paiement();
                paiement.setmontant(montant);
                paiement.setdatePaiement(datePaiement);
                paiement.setmoyenDePaiement(moyen);
 
                paiementService.insertPaiement(paiement);
                chargerPaiements();
                viderChamps();

                JOptionPane.showMessageDialog(null, "Paiement ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
 
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Le montant doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Format de date invalide. Utilisez le format AAAA-MM-DD", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
 
        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                montantChamp.setText(model.getValueAt(i, 1).toString());
                datePaiementChamp.setText(model.getValueAt(i, 2).toString());
            }
        });
 
 
        try {
            chargerPaiements();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des paiements: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }
 
    private void chargerPaiements() throws IOException, InterruptedException {
        model.setRowCount(0);
        Paiements paiements = paiementService.selectPaiements();
        if (paiements != null && paiements.getPaiements() != null) {
            ArrayList<Paiement> list = new ArrayList<>(paiements.getPaiements());
            list.sort(Comparator.comparing(Paiement::getdatePaiement));
            for (Paiement p : list) {
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
}