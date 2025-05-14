package edu.ezip.ing1.pds.requests.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Creneaux;
import edu.ezip.ing1.pds.business.dto.PlanificationExamen;
import edu.ezip.ing1.pds.business.dto.PlanificationExamens;
import edu.ezip.ing1.pds.business.dto.RendezVous;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;

public class SelectDisponibiliteParDateClientRequest extends ClientRequest<PlanificationExamen, Creneaux> {

    public SelectDisponibiliteParDateClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, PlanificationExamen info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public Creneaux readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Creneaux creneaux = mapper.readValue(body, Creneaux.class);
        return creneaux;
    }
}
