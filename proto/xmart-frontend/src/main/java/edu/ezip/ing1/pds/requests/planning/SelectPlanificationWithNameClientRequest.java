package edu.ezip.ing1.pds.requests.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Equipement;
import edu.ezip.ing1.pds.business.dto.Medecins;
import edu.ezip.ing1.pds.business.dto.PlanificationWithNames;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;

public class SelectPlanificationWithNameClientRequest extends ClientRequest<Object, PlanificationWithNames> {


    public SelectPlanificationWithNameClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public PlanificationWithNames readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final PlanificationWithNames planification = mapper.readValue(body, PlanificationWithNames.class);
        return planification;
    }
}
