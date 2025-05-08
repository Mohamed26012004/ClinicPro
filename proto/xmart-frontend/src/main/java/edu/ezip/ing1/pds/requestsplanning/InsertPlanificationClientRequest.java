package edu.ezip.ing1.pds.requestsplanning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.business.dto.PlanificationExamen;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;

public class InsertPlanificationClientRequest extends ClientRequest<PlanificationExamen, String> {

    public InsertPlanificationClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, PlanificationExamen info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);

    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> planificationMap = mapper.readValue(body, Map.class);
        final String result  = planificationMap.get("planification_idPlanification").toString();
        return result;
    }


}
