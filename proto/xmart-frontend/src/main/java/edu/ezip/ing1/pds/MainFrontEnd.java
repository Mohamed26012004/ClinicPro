package edu.ezip.ing1.pds;

import java.io.IOException;

import edu.ezip.ing1.pds.business.dto.Facture;

import java.time.LocalDate;
import java.util.Date;


import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.business.dto.Maintenance;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;

import edu.ezip.ing1.pds.graphics.*;
import edu.ezip.ing1.pds.services.EquipementService;
import edu.ezip.ing1.pds.services.ExamenService;
import edu.ezip.ing1.pds.services.FactureService;
import edu.ezip.ing1.pds.services.MaintenanceService;

public class MainFrontEnd {


    public static void main(String[] args) throws IOException, InterruptedException {
//        final String networkConfigFile = "network.yaml";
//        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
//        final EquipementService equipementService = new EquipementService(networkConfig);
//        System.out.println(equipementService.selectEquipements());
//       System.out.println(equipementService.getTotalCoutParJour());
//        final MaintenanceService maintenanceService = new MaintenanceService(networkConfig);
//        Maintenance maintenance = new Maintenance(2324, "dfsdsfsd", LocalDate.now(), 23);
//        maintenanceService.deleteMaintenance(maintenance);
          //PaiementFront pf= new PaiementFront();


          //TotalCoutFront fen = new TotalCoutFront();
          EquipementFront p= new EquipementFront();
          //MaintenanceFront mf = new MaintenanceFront();
          //TotalMaintenanceFront tmf = new TotalMaintenanceFront();
          //TotalCoutFront tc = new TotalCoutFront();
          ///EquipementFront ef = new EquipementFront();
          //CoutGlobalParJourFront cj = new CoutGlobalParJourFront();
          //CoutGlobalParMoisFront cg = new CoutGlobalParMoisFront();
////
        //Fenetre fene = new Fenetre();
        //fene.setVisible(true);
        //EquipementFront f = new EquipementFront();
//        DiagnosticFront d = new DiagnosticFront();
//        EquipementFront f = new EquipementFront();


//        Date dat = new Date();
//        Facture fac = new Facture(true, dat);
//        final String networkConfigFile = "network.yaml";
//        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
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

    }
}
