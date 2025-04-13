package edu.ezip.ing1.pds;

import java.io.IOException;

import edu.ezip.ing1.pds.business.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;

import edu.ezip.ing1.pds.graphics.DiagnosticFront;
import edu.ezip.ing1.pds.graphics.EquipementFront;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.graphics.PaiementFront;
import edu.ezip.ing1.pds.services.ExamenService;
import edu.ezip.ing1.pds.services.FactureService;
import edu.ezip.ing1.pds.servicesplanning.CreneauService;
import edu.ezip.ing1.pds.servicesplanning.RendezVousService;
import edu.ezip.ing1.pds.servicesplanning.SalleService;

import javax.swing.*;

public class MainFrontEnd {


    public static void main(String[] args) throws IOException, InterruptedException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        Fenetre fen = new Fenetre();
        fen.setVisible(true);

//        EquipementFront f = new EquipementFront();

       // Fenetre fen = new Fenetre();
        //fen.setVisible(true);
//        EquipementFront f = new EquipementFront();
//        DiagnosticFront d = new DiagnosticFront();
//        EquipementFront f = new EquipementFront();


//        Date dat = new Date();
//        Facture fac = new Facture(true, dat);

        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final RendezVousService rdvService = new RendezVousService(networkConfig);
        final SalleService salleService = new SalleService(networkConfig);

//        Salle s = new Salle("A1234566", "Consultation", "Réservé");
//        s.setId(2);
//        salleService.updateSalle(s);
//        final CreneauService creneauService = new CreneauService(networkConfig);
//        final TraitementService traitementService = new TraitementService(networkConfig);
//        final AntecedentMedicalService antecedentMedicalService = new AntecedentMedicalService(networkConfig);
//        final PatientService patientService = new PatientService (networkConfig);

//        Traitement traitement = new Traitement("Antibiotiques","Pour diminuer l'allergie","19/03/2025","20/03/2025");
//        traitementService.insertTraitement(traitement);


//        AntecedentMedical antecedentMedical = new AntecedentMedical("Migraines", "Depuis l'âge de 6 ans",1);
//        antecedentMedicalService.insertAntecedentMedical(antecedentMedical);
//        final FactureService factureService = new FactureService(networkConfig);
//
//        factureService.insertFacture(fac);
//        Factures factures = factureService.selectFactures();
//        System.out.println(factures);

//        LocalDate  date = LocalDate.now();
//        LocalTime debut = LocalTime.of(16, 00);
//        LocalTime fin = LocalTime.of(16, 30);
//
//        RendezVous rendezVous = new RendezVous(4565464, 3, 6, 2, date, debut, fin);
//        rendezVous.setIdRendezVous(6);
//        rdvService.updateRendezVous(rendezVous);

//        RendezVous rdv = new RendezVous();
//        rdv.setDateRendezVous(date);
//        System.out.println(creneauService.selectCreneauxParDate(rdv));
//        Examen examen = new Examen();
//        examen.setId(2);
//        RendezVouss rdvs = rdvService.selectIdRendezVousAndPlanificationParExamen(examen);
//        for (RendezVous r : rdvs.getRdvs()){
//            System.out.println(r.getIdRendezVous());
//        }



    }
}
