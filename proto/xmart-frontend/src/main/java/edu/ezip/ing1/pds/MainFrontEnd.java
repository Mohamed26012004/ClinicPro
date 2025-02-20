package edu.ezip.ing1.pds;

import java.io.IOException;

import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayDeque;
import java.util.Deque;

import edu.ezip.ing1.pds.graphics.Fenetre;

public class MainFrontEnd {

     private final static String LoggingLabel = "FrontEnd";
     private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
     private final static String networkConfigFile = "network.yaml";
     private static final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

    public static void main(String[] args) throws IOException, InterruptedException {
         final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
         logger.debug("Load Network config file : {}", networkConfig.toString());
//
//         final ExamenService examenService = new ExamenService(networkConfig);



//         final StudentService studentService = new StudentService(networkConfig);
//         studentService.insertStudents();
//         Students students = studentService.selectStudents();
//         final AsciiTable asciiTable = new AsciiTable();
//         for (final Student student : students.getStudents()) {
//             asciiTable.addRule();
//             asciiTable.addRow(student.getFirstname(), student.getName(), student.getGroup());
//         }
//         asciiTable.addRule();
//         logger.debug("\n{}\n", asciiTable.render());

        Fenetre fen = new Fenetre();
        fen.setVisible(true);

    }
}
