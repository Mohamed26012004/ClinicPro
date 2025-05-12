package edu.ezip.ing1.pds.services.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Horaire;
import edu.ezip.ing1.pds.business.dto.Horaires;
import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.medecin.SelectHoraireMedecinClientRequest;
import edu.ezip.ing1.pds.requestsplanning.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class HoraireService {

    private final static String LoggingLabel = "FrontEnd - HoraireService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_HORAIRE";
    final String selectRequestOrder = "SELECT_ALL_HORAIRES";
    final String updateRequestOrder = "UPDATE_HORAIRE";
    final String deleteRequestOrder = "DELETE_HORAIRE";
    final String selectHoraireMedecinRequestOrder = "SELECT_HORAIRE_MEDECIN";
    final String selectOneHoraireRequestOrder = "SELECT_ONE_HORAIRE";

    private final NetworkConfig networkConfig;

    public HoraireService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertHoraire(Horaire horaire)throws InterruptedException, IOException {
        insertDeleteUpdateMedecin(horaire, insertRequestOrder);

    }

    public void updateHoraire(Horaire horaire)throws InterruptedException, IOException {
        insertDeleteUpdateMedecin(horaire, updateRequestOrder);
    }

    public void deleteHoraire(Horaire horaire)throws InterruptedException, IOException {
        insertDeleteUpdateMedecin(horaire, deleteRequestOrder);
    }
    public void insertDeleteUpdateMedecin(Horaire horaire, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(horaire);
        logger.trace("Horaire with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertHoraireClientRequest clientRequest = new InsertHoraireClientRequest(
                networkConfig,
                birthdate++, request, horaire, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Horaire h = (Horaire)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {}  --> {}",
                    clientRequest2.getThreadName(),
                    h.getJour(), h.getHeureDebut(), h.getHeureFin(),
                    clientRequest2.getResult());
        }
    }

    public Horaires selectHoraires() throws InterruptedException, IOException {
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
        final SelectAllHorairesClientRequest clientRequest = new SelectAllHorairesClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Horaires) joinedClientRequest.getResult();
        }
        else {
            logger.error("No horaires found");
            return null;
        }
    }

    public Horaires selectHoraireMedecin(Medecin medecin) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(medecin);
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectHoraireMedecinRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectHoraireMedecinClientRequest clientRequest = new SelectHoraireMedecinClientRequest(
                networkConfig,
                birthdate++, request, medecin, requestBytes);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Horaires) joinedClientRequest.getResult();
        } else {
            logger.error("No medecins found");
            return null;
        }
    }

    public Horaire selectOneHoraire(Horaire horaire) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(horaire);
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectOneHoraireRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectAllHorairesClientRequest clientRequest = new SelectAllHorairesClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            Horaires horaires=  (Horaires) joinedClientRequest.getResult();
            Horaire horaireSelected = null;
            int k=1;
            for (Horaire h : horaires.getHoraires()){
                if(k==1){
                    horaireSelected = h;
                }
                k=2;
            }
            return horaireSelected;
        }
        else {
            logger.error("No horaires found");
            return null;
        }
    }


}
