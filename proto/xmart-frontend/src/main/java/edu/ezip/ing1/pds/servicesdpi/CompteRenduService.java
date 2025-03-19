package edu.ezip.ing1.pds.servicesdpi;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import edu.ezip.ing1.pds.business.dto.CompteRendu;
import edu.ezip.ing1.pds.business.dto.CompteRendus;
import edu.ezip.ing1.pds.requestsdpi.InsertCompteRenduClientRequest;
import edu.ezip.ing1.pds.requestsdpi.SelectAllCompteRendusClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;

import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;


public class CompteRenduService {

    private final static String LoggingLabel = "FrontEnd - StudentService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_COMPTE_RENDU";
    final String selectRequestOrder = "SELECT_ALL_COMPTE_RENDUS";
    final String updateRequestOrder = "UPDATE_COMPTE_RENDU";
    final String deleteRequestOrder = "DELETE_COMPTE_RENDU";


    private final NetworkConfig networkConfig;

    public CompteRenduService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertCompteRendu(CompteRendu compteRendu)throws InterruptedException, IOException {
        insertDeleteUpdateCompteRendu(compteRendu, insertRequestOrder);
    }

    public void updateCompteRendu(CompteRendu compteRendu)throws InterruptedException, IOException {
        insertDeleteUpdateCompteRendu(compteRendu, updateRequestOrder);
    }

    public void deleteCompteRendu(CompteRendu compteRendu)throws InterruptedException, IOException {
        insertDeleteUpdateCompteRendu(compteRendu, deleteRequestOrder);
    }

    public void insertDeleteUpdateCompteRendu(CompteRendu compteRendu, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(compteRendu);
        logger.trace("Examen with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertCompteRenduClientRequest clientRequest = new InsertCompteRenduClientRequest(
                networkConfig,
                birthdate++, request, compteRendu, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final CompteRendu cr = (CompteRendu)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {}  --> {}",
                    clientRequest2.getThreadName(),
                    cr.getTypeSymptome(), cr.getDescriptionSymptome(),
                    clientRequest2.getResult());
        }
    }


    public CompteRendus selectcompteRendus() throws InterruptedException, IOException {
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
        final SelectAllCompteRendusClientRequest clientRequest = new SelectAllCompteRendusClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName()); //Une fenêtre pour afficher les CompteRendus
            return (CompteRendus) joinedClientRequest.getResult();
        }
        else {
            logger.error("No medical report found");
            return null;
        }
    }

}

