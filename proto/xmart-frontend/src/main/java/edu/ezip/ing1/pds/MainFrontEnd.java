package edu.ezip.ing1.pds;

import java.io.IOException;

import edu.ezip.ing1.pds.business.dto.Facture;

import java.util.Date;


import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.services.ExamenService;
import edu.ezip.ing1.pds.services.FactureService;

public class MainFrontEnd {


    public static void main(String[] args) throws IOException, InterruptedException {

        Fenetre fen = new Fenetre();
        fen.setVisible(true);
//
//        Date dat = new Date();
//        Facture fac = new Facture(true, dat);
//        final String networkConfigFile = "network.yaml";
//        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
//        final FactureService factureService = new FactureService(networkConfig);
//
//        factureService.insertFacture(fac);
//        Factures factures = factureService.selectFactures();
//        System.out.println(factures);

    }
}





 

