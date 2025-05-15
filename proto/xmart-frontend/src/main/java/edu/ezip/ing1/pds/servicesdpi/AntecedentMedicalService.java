package edu.ezip.ing1.pds.servicesdpi;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import edu.ezip.ing1.pds.business.dto.AntecedentMedical;
import edu.ezip.ing1.pds.business.dto.AntecedentMedicals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;

import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requestsdpi.InsertAntecedentMedicalClientRequest;
import edu.ezip.ing1.pds.requestsdpi.SelectAllAntecedentMedicalsClientRequest;


public class AntecedentMedicalService {

    private final static String LoggingLabel = "FrontEnd - StudentService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_ANTECEDENT_MEDICAL";
    final String selectRequestOrder = "SELECT_ALL_ANTECEDENT_MEDICALS";
    final String updateRequestOrder = "UPDATE_ANTECEDENT_MEDICAL";
    final String deleteRequestOrder = "DELETE_ANTECEDENT_MEDICAL";


    private final NetworkConfig networkConfig;

    public AntecedentMedicalService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertAntecedentMedical(AntecedentMedical antecedentMedical)throws InterruptedException, IOException {
        insertDeleteUpdateAntecedentMedical(antecedentMedical, insertRequestOrder);
    }

    public void updateAntecedentMedical(AntecedentMedical antecedentMedical)throws InterruptedException, IOException {
        insertDeleteUpdateAntecedentMedical(antecedentMedical, updateRequestOrder);
    }

    public void deleteAntecedentMedical(AntecedentMedical antecedentMedical)throws InterruptedException, IOException {
        insertDeleteUpdateAntecedentMedical(antecedentMedical, deleteRequestOrder);
    }

    public void insertDeleteUpdateAntecedentMedical(AntecedentMedical antecedentMedical, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(antecedentMedical);
        logger.trace("Examen with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertAntecedentMedicalClientRequest clientRequest = new InsertAntecedentMedicalClientRequest(
                networkConfig,
                birthdate++, request, antecedentMedical, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final AntecedentMedical am = (AntecedentMedical)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {}  --> {}",
                    clientRequest2.getThreadName(),
                    am.getType_antecedentMedical(), am.getDescription_antecedentMedical(), am.getIdPatient(),
                    clientRequest2.getResult());
        }
    }


    public AntecedentMedicals selectantecedentMedicals() throws InterruptedException, IOException {
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
        final SelectAllAntecedentMedicalsClientRequest clientRequest = new SelectAllAntecedentMedicalsClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName()); //Une fenêtre pour afficher les AntecedentMedicals
            return (AntecedentMedicals) joinedClientRequest.getResult();
        }
        else {
            logger.error("No medical history found");
            return null;
        }
    }

}

