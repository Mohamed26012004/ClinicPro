package edu.ezip.ing1.pds.services.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Creneaux;
import edu.ezip.ing1.pds.business.dto.PlanificationExamen;
import edu.ezip.ing1.pds.business.dto.PlanificationExamens;
import edu.ezip.ing1.pds.business.dto.RendezVous;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.planning.SelectDisponibiliteParDateClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class CreneauService {

    private final static String LoggingLabel = "FrontEnd - HoraireService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String selectRequestOrder = "SELECT_DISPONIBILITE_PAR_DATE";
    final String selectByMedecinRequestOrder = "SELECT_DISPONIBILITE_PAR_DATE_BY_MEDECIN";

    private final NetworkConfig networkConfig;

    public CreneauService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }


    public Creneaux selectCreneaux(PlanificationExamen planificationExamen, String requestOrder) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(planificationExamen);
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectDisponibiliteParDateClientRequest clientRequest = new SelectDisponibiliteParDateClientRequest (
                networkConfig,
                birthdate++, request, planificationExamen, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Creneaux) joinedClientRequest.getResult();
        }
        else {
            logger.error("No creneaux found");
            return null;
        }
    }

    public Creneaux selectCreneauByDate(PlanificationExamen planificationExamen) throws IOException, InterruptedException {
        return selectCreneaux(planificationExamen, selectRequestOrder);
    }
    public Creneaux selectCreneauByDateByMedecin(PlanificationExamen planificationExamen) throws IOException, InterruptedException {
        return selectCreneaux(planificationExamen, selectByMedecinRequestOrder);
    }
}
