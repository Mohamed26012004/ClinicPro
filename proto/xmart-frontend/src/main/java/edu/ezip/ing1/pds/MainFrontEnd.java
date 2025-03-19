package edu.ezip.ing1.pds;

import java.io.IOException;
import java.time.LocalDate;

import edu.ezip.ing1.pds.business.dto.Equipement;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.EquipementService;

public class MainFrontEnd {


    public static void main(String[] args) throws IOException, InterruptedException {
        /*Fenetre fen = new Fenetre();
        fen.setVisible(true);*/

        final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final EquipementService service = new EquipementService(networkConfig);
        LocalDate date = LocalDate.of(2025, 3, 12);
        Equipement equipement = new Equipement(5, "table", date, 4);
        service.insertEquipement(equipement);



        /*final String networkConfigFile = "network.yaml";
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final EquipementService equipementService = new EquipementService(networkConfig);
        System.out.println(equipementService.selectEquipements());
        System.out.println(service.selctequipements());*/


    }
}





 

