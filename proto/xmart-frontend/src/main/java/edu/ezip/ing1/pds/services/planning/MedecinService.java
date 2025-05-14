package edu.ezip.ing1.pds.services.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Consulte;
import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.business.dto.Medecins;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.medecin.InsertConsulteClientRequest;
import edu.ezip.ing1.pds.requests.medecin.InsertMedecinClientRequest;
import edu.ezip.ing1.pds.requests.medecin.SelectAllMedecinsClientRequest;
import edu.ezip.ing1.pds.requests.medecin.SelectInfoMedecinClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.UUID;

public class MedecinService {

    private final static String LoggingLabel = "FrontEnd - MedecinService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_MEDECIN";
    final String selectRequestOrder = "SELECT_ALL_MEDECINS";
    final String updateRequestOrder = "UPDATE_MEDECIN";
    final String deleteRequestOrder = "DELETE_MEDECIN";
    final String selectSpecialiteRequestOrder = "SELECT_SPECIALITE_MEDECIN";
    final String selectMedecinParSpecialiteRequestOrder = "SELECT_MEDECIN_PAR_SPECIALITE";
    final String insertConsulteRequestOrder = "INSERT_CONSULTE";
    final String deleteConsulteRequestOrder = "DELETE_CONSULTE";



    private final NetworkConfig networkConfig;

    public MedecinService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertMedecin(Medecin medecin) throws InterruptedException, IOException {
        insertDeleteUpdateMedecin(medecin, insertRequestOrder);
    }

    public void updateMedecin(Medecin medecin) throws InterruptedException, IOException {
        insertDeleteUpdateMedecin(medecin, updateRequestOrder);
    }

    public void deleteMedecin(Medecin medecin) throws InterruptedException, IOException {
        insertDeleteUpdateMedecin(medecin, deleteRequestOrder);
    }

    public void insertDeleteUpdateMedecin(Medecin medecin, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(medecin);
        logger.trace("Medecin with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertMedecinClientRequest clientRequest = new InsertMedecinClientRequest(
                networkConfig,
                birthdate++, request, medecin, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Medecin m = (Medecin) clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} {} {} {} --> {}",
                    clientRequest2.getThreadName(),
                    m.getNumeroADELI(), m.getNom(), m.getPrenom(), m.getTelephone(),
                    m.getSpecialite(), m.getSalaire(),
                    clientRequest2.getResult());
        }
    }

    public Medecins selectMedecins(String requestOrder) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
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
            return (Medecins) joinedClientRequest.getResult();
        } else {
            logger.error("No medecins found");
            return null;
        }
    }

    public ArrayList<String> selectMedecinParSpecialite(String specilite) throws IOException, InterruptedException {
        ArrayList<String> list = new ArrayList<>();
        Medecin medecin = new Medecin();
        medecin.setSpecialite(specilite);
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(medecin);
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectMedecinParSpecialiteRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectInfoMedecinClientRequest clientRequest = new SelectInfoMedecinClientRequest(
                networkConfig,
                birthdate++, request, medecin, requestBytes);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            Medecins medecins = (Medecins) joinedClientRequest.getResult();
            for (Medecin m : medecins.getMedecins()) {
                list.add(m.getNom() + " " + m.getPrenom());
            }
            return list;
        } else {
            logger.error("No medecins found");
            return null;
        }

    }

    public Medecins selectAllMedecins() throws IOException, InterruptedException {
        return selectMedecins(selectRequestOrder);
    }

    public ArrayList<String> selectSpecialiteMedecin() throws IOException, InterruptedException {
        ArrayList<String> list = new ArrayList<>();
        Medecins medecins = selectMedecins(selectSpecialiteRequestOrder);
        System.out.println(medecins);
        for (Medecin m : medecins.getMedecins()) {
            list.add(m.getSpecialite());
        }
        return list;
    }

    public void insertDeleteConsulte(Consulte consulte, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(consulte);
        logger.trace("Medecin with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertConsulteClientRequest clientRequest = new InsertConsulteClientRequest(
                networkConfig,
                birthdate++, request, consulte, requestBytes);
        clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Consulte c = (Consulte) clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} --> {}",
                    clientRequest2.getThreadName(),
                    c.getNumeroADELI(), c.getId(),
                    clientRequest2.getResult());
        }
    }

    public void insertConsulte(Consulte consulte) throws IOException, InterruptedException {
        insertDeleteConsulte(consulte, insertConsulteRequestOrder);
    }

    public void deleteConsulte(Consulte consulte) throws IOException, InterruptedException {
        insertDeleteConsulte(consulte, deleteConsulteRequestOrder);
    }




}

