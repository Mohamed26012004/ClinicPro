package edu.ezip.ing1.pds.services.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Patient;
import edu.ezip.ing1.pds.business.dto.Patients;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requestsplanning.InsertPatientClientRequest;
import edu.ezip.ing1.pds.requestsplanning.SelectAllPatientsClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class PatientService {

    private final static String LoggingLabel = "FrontEnd - PatientService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_PATIENT";
    final String selectRequestOrder = "SELECT_ALL_PATIENTS";
    final String updateRequestOrder = "UPDATE_PATIENT";
    final String deleteRequestOrder = "DELETE_PATIENT";


    private final NetworkConfig networkConfig;

    public PatientService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertPatient(Patient patient)throws InterruptedException, IOException {
        insertDeleteUpdatePatient(patient, insertRequestOrder);
    }

    public void updatePatient(Patient patient)throws InterruptedException, IOException {
        insertDeleteUpdatePatient(patient, updateRequestOrder);
    }

    public void deletePatient(Patient patient)throws InterruptedException, IOException {
        insertDeleteUpdatePatient(patient, deleteRequestOrder);
    }
    public void insertDeleteUpdatePatient(Patient patient, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(patient);
        logger.trace("Patient with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertPatientClientRequest clientRequest = new InsertPatientClientRequest(
                networkConfig,
                birthdate++, request, patient, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Patient p = (Patient)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} {} --> {}",
                    clientRequest2.getThreadName(),
                    p.getNom(), p.getPrenom(), p.getTelephone(),
                    p.getAdresse(),
                    clientRequest2.getResult());
        }
    }

    public Patients selectPatients() throws InterruptedException, IOException {
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
        final SelectAllPatientsClientRequest clientRequest = new SelectAllPatientsClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Patients) joinedClientRequest.getResult();
        }
        else {
            logger.error("No Medecins found");
            return null;
        }
    }
}
