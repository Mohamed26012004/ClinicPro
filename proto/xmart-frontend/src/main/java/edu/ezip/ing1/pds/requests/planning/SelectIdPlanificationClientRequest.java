package edu.ezip.ing1.pds.requests.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;

public class SelectIdPlanificationClientRequest extends ClientRequest<PlanificationExamen,PlanificationExamens> {

    public SelectIdPlanificationClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, PlanificationExamen info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    public PlanificationExamens readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final PlanificationExamens planificationExamens = mapper.readValue(body, PlanificationExamens.class);
        return planificationExamens;
    }
}
