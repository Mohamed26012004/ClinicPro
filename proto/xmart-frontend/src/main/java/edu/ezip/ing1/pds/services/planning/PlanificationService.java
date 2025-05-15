package edu.ezip.ing1.pds.services.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.PlanificationExamen;
import edu.ezip.ing1.pds.business.dto.PlanificationExamens;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.planning.InsertPlanificationClientRequest;
import edu.ezip.ing1.pds.requests.planning.SelectIdPlanificationClientRequest;
import edu.ezip.ing1.pds.requests.planning.SelectOnePlanificationClientRequest;
import edu.ezip.ing1.pds.requests.planning.SelectPlanificationClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class PlanificationService {


    private final static String LoggingLabel = "FrontEnd - PlanificationExamen";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_PLANIFICATION";
    final String selectRequestOrder = "SELECT_PLANIFICATION";
    final String updateRequestOrder = "UPDATE_PLANIFICATION";
    final String deleteRequestOrder = "DELETE_PLANIFICATION";
    final String selectIdPlanParMedecin = "SELECT_ID_PLANIFICATION_PAR_MEDECIN";
    final String selectIdPlanParExamen = "SELECT_ID_PLANIFICATION_PAR_EXAMEN";
    final String selectOnePlanificationRequestOrder = "SELECT_ONE_PLANIFICATION";


    private final NetworkConfig networkConfig;

    public PlanificationService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertPlanification(PlanificationExamen planificationExamen) throws InterruptedException, IOException {
        insertDeleteUpdatePlanification(planificationExamen, insertRequestOrder);
    }

    public void updatePlanification(PlanificationExamen planificationExamen) throws InterruptedException, IOException {
        insertDeleteUpdatePlanification(planificationExamen, updateRequestOrder);
    }

    public void deletePlanification(PlanificationExamen planificationExamen) throws InterruptedException, IOException {
        insertDeleteUpdatePlanification(planificationExamen, deleteRequestOrder);
    }

    public void insertDeleteUpdatePlanification(PlanificationExamen planificationExamen, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(planificationExamen);
        logger.trace("Salle with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertPlanificationClientRequest clientRequest = new  InsertPlanificationClientRequest(
                networkConfig,
                birthdate++, request, planificationExamen, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final PlanificationExamen plan = (PlanificationExamen) clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} {} {} {} {} --> {}",
                    clientRequest2.getThreadName(),
                    plan.getNumeroADELI(), plan.getIdPatient(), plan.getIdExamen(), plan.getIdSalle(),
                    plan.getDatePlanification(), plan.getHeureDebut(), plan.getHeureFin(),
                    clientRequest2.getResult());
        }
    }

    public PlanificationExamens selectPlanifications() throws InterruptedException, IOException {
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
        final SelectPlanificationClientRequest clientRequest = new SelectPlanificationClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (PlanificationExamens) joinedClientRequest.getResult();
        } else {
            logger.error("No Salles found");
            return null;
        }
    }

    public PlanificationExamens selectIdPlanification(PlanificationExamen planificationExamen, String requestOrder) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(planificationExamen);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectIdPlanificationClientRequest clientRequest = new SelectIdPlanificationClientRequest(
                networkConfig,
                birthdate++, request, planificationExamen, requestBytes);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (PlanificationExamens) joinedClientRequest.getResult();
        } else {
            logger.error("No Salles found");
            return null;
        }
    }

    public PlanificationExamens selectIdPlanificationParExamen(PlanificationExamen planificationExamen) throws IOException, InterruptedException {
        return selectIdPlanification(planificationExamen, selectIdPlanParExamen);
    }
    public PlanificationExamens selectIdPlanificationParMedecin(PlanificationExamen planificationExamen) throws IOException, InterruptedException {
        return selectIdPlanification(planificationExamen, selectIdPlanParMedecin);
    }

    public PlanificationExamen selectOnePlanifications(PlanificationExamen planificationExamen) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(planificationExamen);
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectOnePlanificationRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectOnePlanificationClientRequest clientRequest = new SelectOnePlanificationClientRequest(
                networkConfig,
                birthdate++, request, planificationExamen, requestBytes);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            PlanificationExamens planificationExamens = (PlanificationExamens) joinedClientRequest.getResult();
            for (PlanificationExamen p : planificationExamens.getPlanifications()) {
                return p;
            }
        } else {
            logger.error("No Salles found");
            return null;
        }
        return planificationExamen;
    }

}
