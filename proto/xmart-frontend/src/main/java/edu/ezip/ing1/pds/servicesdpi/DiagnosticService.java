package edu.ezip.ing1.pds.servicesdpi;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import edu.ezip.ing1.pds.business.dto.Diagnostic;
import edu.ezip.ing1.pds.business.dto.Diagnostics;
import edu.ezip.ing1.pds.requestsdpi.InsertDiagnosticClientRequest;
import edu.ezip.ing1.pds.requestsdpi.SelectAllDiagnosticsClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;

import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;


public class DiagnosticService {

    private final static String LoggingLabel = "FrontEnd - StudentService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_DIAGNOSTIC";
    final String selectRequestOrder = "SELECT_ALL_DIAGNOSTICS";
    final String updateRequestOrder = "UPDATE_DIAGNOSTIC";
    final String deleteRequestOrder = "DELETE_DIAGNOSTIC";


    private final NetworkConfig networkConfig;

    public DiagnosticService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertDiagnostic(Diagnostic diagnostic)throws InterruptedException, IOException {
        insertDeleteUpdateDiagnostic(diagnostic, insertRequestOrder);
    }

    public void updateDiagnostic(Diagnostic diagnostic)throws InterruptedException, IOException {
        insertDeleteUpdateDiagnostic(diagnostic, updateRequestOrder);
    }

    public void deleteDiagnostic(Diagnostic diagnostic)throws InterruptedException, IOException {
        insertDeleteUpdateDiagnostic(diagnostic, deleteRequestOrder);
    }

    public void insertDeleteUpdateDiagnostic(Diagnostic diagnostic, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(diagnostic);
        logger.trace("Examen with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertDiagnosticClientRequest clientRequest = new InsertDiagnosticClientRequest(
                networkConfig,
                birthdate++, request, diagnostic, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Diagnostic d = (Diagnostic)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} {}  --> {}",
                    clientRequest2.getThreadName(),
                    d.getIdPlanification(), d.getCodeCIM10(), d.getNomMaladie(), d.getDescription_Diagnostic(),
                    clientRequest2.getResult());
        }
    }


    public Diagnostics selectDiagnostics() throws InterruptedException, IOException {
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
        final SelectAllDiagnosticsClientRequest clientRequest = new SelectAllDiagnosticsClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName()); //Une fenêtre pour afficher les Diagnostics
            return (Diagnostics) joinedClientRequest.getResult();
        }
        else {
            logger.error("No diagnosis found");
            return null;
        }
    }

}

