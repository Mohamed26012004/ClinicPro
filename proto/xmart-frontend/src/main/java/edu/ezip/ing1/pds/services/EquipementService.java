package edu.ezip.ing1.pds.services;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Equipement;
import edu.ezip.ing1.pds.business.dto.Equipements;

import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.InsertEquipementClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllEquipementsClientRequest;




public class EquipementService {

    private final static String LoggingLabel = "FrontEnd - StudentService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    
    final String insertRequestOrder = "INSERT_EQUIPEMENT";
    final String selectRequestOrder = "SELECT_ALL_EQUIPEMENTS";
    final String updateRequestOrder = "UPDATE_EQUIPEMENT";
    final String deleteRequestOrder = "DELETE_EQUIPEMENT";

    private final NetworkConfig networkConfig;

    public EquipementService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertEquipement(Equipement equipement)throws InterruptedException, IOException {
        insertDeleteUpdateEquipement(equipement, insertRequestOrder);
    }

    public void updateExamen(Equipement equipement)throws InterruptedException, IOException {
        insertDeleteUpdateEquipement(equipement, updateRequestOrder);
    }

    public void deleteEquipement(Equipement equipement)throws InterruptedException, IOException {
        insertDeleteUpdateEquipement(equipement, deleteRequestOrder);
    }
    
    public void insertDeleteUpdateEquipement(Equipement equipement, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
       
        int birthdate = 0;
       
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(equipement);
            logger.trace("Equipement with its JSON face : {}", jsonifiedGuy);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(requestOrder);
            request.setRequestContent(jsonifiedGuy);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertEquipementClientRequest clientRequest = new InsertEquipementClientRequest(
                    networkConfig,
                    birthdate++, request, equipement, requestBytes);
            clientRequests.push(clientRequest);
        

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Equipement equipements = (Equipement)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientRequest2.getThreadName(),
                    equipement.getNomEquipement(), equipement.getCoutEquipement(), equipement.getDateEquipement(),
                    clientRequest2.getResult());
        }
    }


    public Equipements selectEquipements() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectAllEquipementsClientRequest clientRequest = new SelectAllEquipementsClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName()); //Une fenêtre pour afficher les examens
            return (Equipements) joinedClientRequest.getResult();
        }
        else {
            logger.error("No students found");
            return null;
        }
    }

}
