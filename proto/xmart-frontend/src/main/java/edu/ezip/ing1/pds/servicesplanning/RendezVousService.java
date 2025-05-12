package edu.ezip.ing1.pds.servicesplanning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requestsplanning.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class RendezVousService {

    private final static String LoggingLabel = "FrontEnd - RendezVousService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_RENDEZ_VOUS";
    final String selectRequestOrder = "SELECT_ALL_RENDEZ_VOUS";
    final String updateRequestOrder = "UPDATE_RENDEZ_VOUS";
    final String deleteRequestOrder = "DELETE_RENDEZ_VOUS";
    final String selectIdRendezVousAndPlanificationRequestOrder = "SELECT_ID_RENDEZ_VOUS_AND_PLANIFICATION_PAR_EXAMEN";
    final String selectIdRDVAndPlanMedecinRequestOrder = "SELECT_ID_RENDEZ_VOUS_AND_PLANIFICATION_PAR_MEDECIN";


    private final NetworkConfig networkConfig;
    public RendezVousService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertRendezVous(RendezVous rdv)throws InterruptedException, IOException {
        insertDeleteUpdateRendezVous(rdv, insertRequestOrder);
    }

    public void updateRendezVous(RendezVous rdv)throws InterruptedException, IOException {
        insertDeleteUpdateRendezVous(rdv, updateRequestOrder);
    }

    public void deleteRendezVous(RendezVous rdv)throws InterruptedException, IOException {
        insertDeleteUpdateRendezVous(rdv, deleteRequestOrder);
    }
    public void insertDeleteUpdateRendezVous(RendezVous rdv, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rdv);
        logger.trace("RendezVous with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertRendezVousClientRequest clientRequest = new InsertRendezVousClientRequest(
                networkConfig,
                birthdate++, request, rdv, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final RendezVous r = (RendezVous)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} {} {} {} --> {}",
                    clientRequest2.getThreadName(),
                    r.getNumeroADELI(), r.getIdPatient(), r.getIdExamen(), r.getDateRendezVous(),
                    r.getHeureDebut(), r.getHeureFin(),
                    clientRequest2.getResult());
        }
    }

    public RendezVouss selectAllRendezVous() throws InterruptedException, IOException {
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
        final SelectAllRendezVousClientRequest clientRequest = new SelectAllRendezVousClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (RendezVouss) joinedClientRequest.getResult();
        }
        else {
            logger.error("No horaires found");
            return null;
        }
    }

    public RendezVouss selectIdRendezVousAndPlanificationParExamen(RendezVous rdv) throws InterruptedException, IOException {
       return selectIdRendezVousAndPlanification(rdv,selectIdRendezVousAndPlanificationRequestOrder);
    }

    public RendezVouss selectIdRendezVousAndPlanificationParMedecin(RendezVous rdv) throws InterruptedException, IOException {
        return selectIdRendezVousAndPlanification(rdv,selectIdRDVAndPlanMedecinRequestOrder);
    }

    public RendezVouss selectIdRendezVousAndPlanification(RendezVous rdv,String requestOrder) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rdv);
        final String requestId = UUID.randomUUID().toString();

        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestContent(jsonifiedGuy);
        request.setRequestOrder(requestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectIdRendezVousAndPlanificationClientRequest clientRequest = new SelectIdRendezVousAndPlanificationClientRequest(
                networkConfig,
                birthdate++, request, rdv, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (RendezVouss) joinedClientRequest.getResult();
        }
        else {
            logger.error("No horaires found");
            return null;
        }

    }



}
