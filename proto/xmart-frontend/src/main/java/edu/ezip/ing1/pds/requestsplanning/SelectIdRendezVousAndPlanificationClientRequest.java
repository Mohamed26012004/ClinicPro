package edu.ezip.ing1.pds.requestsplanning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;

public class SelectIdRendezVousAndPlanificationClientRequest extends ClientRequest<RendezVous, RendezVouss> {

    public SelectIdRendezVousAndPlanificationClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, RendezVous info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    public RendezVouss readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final RendezVouss rdvs = mapper.readValue(body, RendezVouss.class);
        return rdvs;
    }
}
