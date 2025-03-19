package edu.ezip.ing1.pds.requestsplanning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Horaire;

import edu.ezip.ing1.pds.business.dto.Horaires;
import edu.ezip.ing1.pds.business.dto.Medecins;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;

public class SelectAllHorairesClientRequest extends ClientRequest<Object, Horaires> {

    public SelectAllHorairesClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public  Horaires readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Horaires horaires = mapper.readValue(body, Horaires.class);
        return horaires;
    }
}
