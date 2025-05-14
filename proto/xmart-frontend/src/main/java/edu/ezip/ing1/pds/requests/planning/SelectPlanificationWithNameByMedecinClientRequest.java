package edu.ezip.ing1.pds.requests.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.PlanificationExamen;
import edu.ezip.ing1.pds.business.dto.PlanificationWithNames;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;

public class SelectPlanificationWithNameByMedecinClientRequest extends ClientRequest<PlanificationExamen, PlanificationWithNames> {

    public SelectPlanificationWithNameByMedecinClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, PlanificationExamen info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public PlanificationWithNames readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final PlanificationWithNames planification = mapper.readValue(body, PlanificationWithNames.class);
        return planification;
    }
}
