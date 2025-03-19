package edu.ezip.ing1.pds;

import java.io.IOException;

import edu.ezip.ing1.pds.business.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.graphics.Fenetre;
import edu.ezip.ing1.pds.medecingrahics.FrameCreationSalle;
import edu.ezip.ing1.pds.medecingrahics.InsertUpdateMedecinWindows;
import edu.ezip.ing1.pds.services.ExamenService;
import edu.ezip.ing1.pds.services.FactureService;
import edu.ezip.ing1.pds.servicesplanning.*;

public class MainFrontEnd {


    public static void main(String[] args) throws IOException, InterruptedException {

        Fenetre fen = new Fenetre();
        fen.setVisible(true);
//        FrameCreationSalle fen = new FrameCreationSalle();
//
//        Date dat = new Date();
//        Facture fac = new Facture(true, dat);
//        final String networkConfigFile = "network.yaml";
//        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
//        final HoraireService service = new HoraireService(networkConfig);
//        final ExamenService examenService = new ExamenService(networkConfig);
//        final RendezVousService rdvService = new RendezVousService(networkConfig);
//        final MedecinService medecinService = new MedecinService(networkConfig);
//        final PatientService patientService = new PatientService(networkConfig);
//        final SalleService salleService = new SalleService(networkConfig);
//        final RendezVousService rdvService = new RendezVousService(networkConfig);

//        Consulte consulte = new Consulte(0,1);
//        medecinService.insertConsulte(consulte);
//        Medecin medecin = new Medecin();
//        medecin.setNumeroADELI(0);
//        System.out.println(service.selectHoraireMedecin(medecin));
//        InsertUpdateMedecinWindows fen = new InsertUpdateMedecinWindows(null);

//        LocalTime debut = LocalTime.of(12, 12);
//        LocalTime fin = LocalTime.of(13, 13);
//
////        Horaire h = new Horaire("Jeudi", debut, fin);
////        service.insertHoraire(h);
////        System.out.println(service.selectOneHoraire(h));
//
//        LocalTime duree = LocalTime.of(00, 30);
//
//        LocalDate date = LocalDate.of(2015, 12, 16);
//
//        RendezVous rdv = new RendezVous(4565464, 3, 2, 2, date, debut, fin);
//        rdv.setIdRendezVous(1);
//        rdv.setIdPatient(1);
//        rdvService.updateRendezVous(rdv);
//        rdv.setNumeroADELI(4565464);
//        rdv.setIdRendezVous(3);
//        rdvService.updateRendezVous(rdv);
//        System.out.println(rdvService.selectAllRendezVous());

//        System.out.println(medecinService.selectSpecialiteMedecin());
//        System.out.println("Les spécialités des Medecins");
//        System.out.println(medecinService.selectMedecinParSpecialite("Dentiste"));
//        Patient p = new Patient("SOUPGUI", "Soupgui", "242423", "11 RUE ");
//        p.setIdPatient(4);
//        p.setNom("SOUPGUIIIIII");
//        patientService.deletePatient(p);
//        System.out.println(patientService.selectPatients());

//        System.out.println(medecinService.selectMedecins());
//        final SalleService service = new SalleService(networkConfig);
//        System.out.println(service.selectSalles());
//
//        Salle salle = new Salle("S12", "Operation", "indispo");
//        salle.setStatut("Maintenance");
//        salleService.deleteSalle(salle);

//        Medecin m = new Medecin(123456, "SMAX", "Max", "12313231", "INGENIEUR", 132313);
//        medecinService.insertMedecin(m);
//        m.setNom("AUTRE NOM");
//        medecinService.deleteMedecin(m);
//        System.out.println(medecinService.selectAllMedecins());

//
//        LocalTime heure = LocalTime.of(12, 12);
//        Examen x = new Examen("Azerty", 53399, heure);
//        x.setId(2);
//        x.setCout(1123);
////        System.out.println(examenService.selectOneExamen(x));
//        examenService.updateExamen(x);

//        DateTimeFormatter formattage = DateTimeFormatter.ofPattern("HH:mm");
//        String debut = "10:15";
//        String fin = "17:17";
//
//        LocalTime heureDebut = LocalTime.parse(debut, formattage);
//        LocalTime heureFin = LocalTime.parse(fin, formattage);
//
//        Horaire horaire = new Horaire("Vendredi", heureDebut, heureFin);
//        horaire.setId(4);
//        horaire.setJour("Dimanche");
//        service.updateHoraire(horaire);
//        horaire.setId(1);
//        System.out.println(service.selectHoraires());
//        service.deleteHoraire(horaire);





//        factureService.insertFacture(fac);
//        Factures factures = factureService.selectFactures();
//        System.out.println(factures);

    }
}
