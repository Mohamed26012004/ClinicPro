package edu.ezip.ing1.pds.requestsplanning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.PlanificationExamens;
import edu.ezip.ing1.pds.business.dto.Salles;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;

public class SelectPlanificationClientRequest extends ClientRequest<Object, PlanificationExamens> {

    public SelectPlanificationClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public PlanificationExamens readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final PlanificationExamens planificationExamens = mapper.readValue(body, PlanificationExamens.class);
        return planificationExamens;
    }
}
