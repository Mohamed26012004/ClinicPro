package edu.ezip.ing1.pds.servicesdpi;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import edu.ezip.ing1.pds.business.dto.Traitement;
import edu.ezip.ing1.pds.business.dto.Traitements;
import edu.ezip.ing1.pds.requestsdpi.InsertTraitementClientRequest;
import edu.ezip.ing1.pds.requestsdpi.SelectAllTraitementsClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;

import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;


public class TraitementService {

    private final static String LoggingLabel = "FrontEnd - StudentService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_TRAITEMENT";
    final String selectRequestOrder = "SELECT_ALL_TRAITEMENTS";
    final String updateRequestOrder = "UPDATE_TRAITEMENT";
    final String deleteRequestOrder = "DELETE_TRAITEMENT";


    private final NetworkConfig networkConfig;

    public TraitementService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertTraitement(Traitement traitement)throws InterruptedException, IOException {
        insertDeleteUpdateTraitement(traitement, insertRequestOrder);
    }

    public void updateTraitement(Traitement traitement)throws InterruptedException, IOException {
        insertDeleteUpdateTraitement(traitement, updateRequestOrder);
    }

    public void deleteTraitement(Traitement traitement)throws InterruptedException, IOException {
        insertDeleteUpdateTraitement(traitement, deleteRequestOrder);
    }

    public void insertDeleteUpdateTraitement(Traitement traitement, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(traitement);
        logger.trace("Examen with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertTraitementClientRequest clientRequest = new InsertTraitementClientRequest(
                networkConfig,
                birthdate++, request, traitement, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Traitement t = (Traitement)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} {}  --> {}",
                    clientRequest2.getThreadName(),
                    t.getType_Traitement(), t.getDescription_Traitement(), t.getDebut_Traitement(), t.getFin_Traitement(),
                    clientRequest2.getResult());
        }
    }


    public Traitements selectTraitements() throws InterruptedException, IOException {
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
        final SelectAllTraitementsClientRequest clientRequest = new SelectAllTraitementsClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName()); //Une fenêtre pour afficher les Traitements
            return (Traitements) joinedClientRequest.getResult();
        }
        else {
            logger.error("No treatment found");
            return null;
        }
    }

}

