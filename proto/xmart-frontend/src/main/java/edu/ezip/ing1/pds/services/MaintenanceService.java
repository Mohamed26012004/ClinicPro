package edu.ezip.ing1.pds.services;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import edu.ezip.ing1.pds.business.dto.TotalMaintenances;
import edu.ezip.ing1.pds.requests.TotalMaintenanceParJourClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Maintenance;
import edu.ezip.ing1.pds.business.dto.Maintenances;

import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.InsertMaintenanceClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllMaintenancesClientRequest;




public class MaintenanceService {
    private final static String LoggingLabel = "FrontEnd - MaintenanceService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_MAINTENANCE";
    final String selectRequestOrder = "SELECT_ALL_MAINTENANCES";
    final String updateRequestOrder = "UPDATE_MAINTENANCE";
    final String deleteRequestOrder = "DELETE_MAINTENANCE";
    final String totalMaintenanceParJourRequestOrder = "TOTAL_MAINTENANCE_PAR_JOUR";


    private final NetworkConfig networkConfig;

    public MaintenanceService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertMaintenance(Maintenance maintenance)throws InterruptedException, IOException {
        insertDeleteUpdateMaintenance(maintenance, insertRequestOrder);
    }

    public void updateMaintenance(Maintenance maintenance)throws InterruptedException, IOException {
        insertDeleteUpdateMaintenance(maintenance, updateRequestOrder);
    }

    public void deleteMaintenance(Maintenance maintenance)throws InterruptedException, IOException {
        insertDeleteUpdateMaintenance(maintenance, deleteRequestOrder);
    }

    public void insertDeleteUpdateMaintenance(Maintenance maintenance, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(maintenance);
        logger.trace("Maintenance with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertMaintenanceClientRequest clientRequest = new InsertMaintenanceClientRequest(
                networkConfig,
                birthdate++, request, maintenance, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Maintenance maintenances = (Maintenance)clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientRequest2.getThreadName(),
                    maintenance.getTypeMaintenance(), maintenance.getCoutMaintenance(), maintenance.getDateMaintenance(),
                    clientRequest2.getResult());
        }
    }


    public Maintenances selectMaintenances() throws InterruptedException, IOException {
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
        final SelectAllMaintenancesClientRequest clientRequest = new SelectAllMaintenancesClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName()); //Une fenêtre pour afficher les examens
            return (Maintenances) joinedClientRequest.getResult();
        }
        else {
            logger.error("No Maintenances found");
            return null;
        }
    }
    public TotalMaintenances getTotalMaintenanceParJour() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(totalMaintenanceParJourRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);

        final TotalMaintenanceParJourClientRequest clientRequest = new TotalMaintenanceParJourClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (TotalMaintenances) joinedClientRequest.getResult();
        } else {
            logger.error("No total costs found");
            return null;
        }
    }


}


