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
import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.Examens;

import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.InsertExamenClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllExamensClientRequest;


public class ExamenService {

    private final static String LoggingLabel = "FrontEnd - StudentService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    
    final String insertRequestOrder = "INSERT_EXAMEN";
    final String selectRequestOrder = "SELECT_ALL_EXAMENS";
    final String updateRequestOrder = "UPDATE_EXAMEN";
    final String deleteRequestOrder = "DELETE_EXAMEN";
    final String selectOneRequestOrder = "SELECT_ONE_EXAMEN";

    private final NetworkConfig networkConfig;

    public ExamenService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertExamen(Examen examen)throws InterruptedException, IOException {
        insertDeleteUpdateExamen(examen, insertRequestOrder);
    }

    public void updateExamen(Examen examen)throws InterruptedException, IOException {
        insertDeleteUpdateExamen(examen, updateRequestOrder);
    }

    public void deleteExamen(Examen examen)throws InterruptedException, IOException {
        insertDeleteUpdateExamen(examen, deleteRequestOrder);
    }
    
    public void insertDeleteUpdateExamen(Examen examen, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
       
        int birthdate = 0;
       
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(examen);
            logger.trace("Examen with its JSON face : {}", jsonifiedGuy);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(requestOrder);
            request.setRequestContent(jsonifiedGuy);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertExamenClientRequest clientRequest = new InsertExamenClientRequest(
                    networkConfig,
                    birthdate++, request, examen, requestBytes);
            clientRequests.push(clientRequest);
        

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Examen exam = (Examen)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientRequest2.getThreadName(),
                    exam.getNom(), exam.getCout(), exam.getNumeroSalle(),
                    clientRequest2.getResult());
        }
    }

    public Examen selectOneExamen(Examen examen) throws InterruptedException, IOException{

        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(examen);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectOneRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectAllExamensClientRequest clientRequest = new SelectAllExamensClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            Examens examens = (Examens) joinedClientRequest.getResult();
            Examen examSelected = null;
            int k=1;
            for (Examen e : examens.getExamens()) {
                if(k==1){
                    examSelected = e;
                }
                k=2;
            }
            return examSelected;
        }
        else {
            logger.error("No students found");
            return null;
        }
    }

    public Examens selectExamens() throws InterruptedException, IOException {
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
        final SelectAllExamensClientRequest clientRequest = new SelectAllExamensClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName()); //Une fenêtre pour afficher les examens
            return (Examens) joinedClientRequest.getResult();
        }
        else {
            logger.error("No students found");
            return null;
        }
    }

}
