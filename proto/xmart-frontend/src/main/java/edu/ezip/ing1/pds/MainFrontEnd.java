package edu.ezip.ing1.pds;

import java.io.IOException;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Facture;

import java.time.LocalTime;
import java.util.Date;


import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.business.dto.Paiement;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.medecingrahics.InsertUpdateMedecinWindows;
import edu.ezip.ing1.pds.services.ExamenService;
import edu.ezip.ing1.pds.services.FactureService;
import edu.ezip.ing1.pds.services.PaiementService;

public class MainFrontEnd {


    public static void main(String[] args) throws IOException, InterruptedException {

//        Fenetre fen = new Fenetre();
//        fen.setVisible(true);

//        Date dat = new Date();
//        Facture fac = new Facture(true, dat);
        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
////        final FactureService factureService = new FactureService(networkConfig);
//        final ExamenService examenService = new ExamenService(networkConfig);
        final PaiementService paiementService = new PaiementService(networkConfig);
//        InsertUpdateMedecinWindows fen = new InsertUpdateMedecinWindows(null);
        Paiement p = new Paiement(9999, "131232132133", "dsfsf");
        p.setidPaiement(4);
        paiementService.updatePaiement(p);
//        System.out.println(paiementService.selectPaiements());
//        Examen examen =  new Examen("INSERt", 56464, LocalTime.of(12, 12));
//        examen.setNom("AAZQSFSDFSF");
//        examenService.insertExamen(examen);
////
//        factureService.insertFacture(fac);
//        Factures factures = factureService.selectFactures();
//        System.out.println(factures);

    }
}
