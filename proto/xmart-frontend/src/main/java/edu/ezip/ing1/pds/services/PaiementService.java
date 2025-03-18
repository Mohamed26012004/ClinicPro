package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Paiement;
import edu.ezip.ing1.pds.business.dto.Paiements;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.InsertPaiementsClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllPaiementsClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class PaiementService {

    private final static String LoggingLabel = "FrontEnd - StudentService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_PAIEMENT";
    final String selectRequestOrder = "SELECT_ALL_PAIEMENTS";
    final String updateRequestOrder = "UPDATE_PAIEMENT";
    final String deleteRequestOrder = "DELETE_PAIEMENT";

    private final NetworkConfig networkConfig;

    public PaiementService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertPaiement(Paiement paiement)throws InterruptedException, IOException {
        insertDeleteUpdatePaiement(paiement, insertRequestOrder);
    }

    public void updatePaiement(Paiement paiement)throws InterruptedException, IOException {
        insertDeleteUpdatePaiement(paiement, updateRequestOrder);
    }

    public void deletePaiement(Paiement paiement)throws InterruptedException, IOException {
        insertDeleteUpdatePaiement(paiement, deleteRequestOrder);
    }

    public void insertDeleteUpdatePaiement(Paiement paiement, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(paiement);
        logger.trace("Paiement with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertPaiementsClientRequest clientRequest = new InsertPaiementsClientRequest(
                networkConfig,
                birthdate++, request, paiement, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Paiement paie = (Paiement)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientRequest2.getThreadName(),
                    paie.getidPaiement(), paie.getmontant(), paie.getdatePaiement(), paie.getmoyenDePaiement(),
                    clientRequest2.getResult());
        }
    }

    public Paiements selectPaiements() throws InterruptedException, IOException {
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
        final SelectAllPaiementsClientRequest clientRequest = new SelectAllPaiementsClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Paiements) joinedClientRequest.getResult();
        }
        else {
            logger.error("Pas de paiements trouvées");
            return null;
        }


    }
}