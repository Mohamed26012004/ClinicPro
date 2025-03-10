package edu.ezip.ing1.pds.servicesplanning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Medecins;
import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.business.dto.Salles;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import edu.ezip.ing1.pds.requestsplanning.InsertSalleClientRequest;
import edu.ezip.ing1.pds.requestsplanning.SelectAllMedecinsClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class SalleService {

    private final static String LoggingLabel = "FrontEnd - SalleService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_SALLE";
    final String selectRequestOrder = "SELECT_ALL_SALLES";
    final String updateRequestOrder = "UPDATE_SALLE";
    final String deleteRequestOrder = "DELETE_SALLE";


    private final NetworkConfig networkConfig;

    public SalleService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertSalle(Salle salle) throws InterruptedException, IOException {
        insertDeleteUpdateSalle(salle, insertRequestOrder);
    }

    public void updateSalle(Salle salle) throws InterruptedException, IOException {
        insertDeleteUpdateSalle(salle, updateRequestOrder);
    }

    public void deleteSalle(Salle salle) throws InterruptedException, IOException {
        insertDeleteUpdateSalle(salle, deleteRequestOrder);
    }

    public void insertDeleteUpdateSalle(Salle salle, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(salle);
        logger.trace("Salle with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertSalleClientRequest clientRequest = new InsertSalleClientRequest(
                networkConfig,
                birthdate++, request, salle, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Salle s = (Salle) clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} {} --> {}",
                    clientRequest2.getThreadName(),
                    s.getId(), s.getNumeroSalle(), s.getTypeSalle(), s.getStatut(),
                    clientRequest2.getResult());
        }
    }

    public Salles selectSalles() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectAllMedecinsClientRequest clientRequest = new SelectAllMedecinsClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Salles) joinedClientRequest.getResult();
        } else {
            logger.error("No Salles found");
            return null;
        }
    }
}
