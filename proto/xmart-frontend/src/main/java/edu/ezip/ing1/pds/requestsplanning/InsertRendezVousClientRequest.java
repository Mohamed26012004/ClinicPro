package edu.ezip.ing1.pds.requestsplanning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.business.dto.RendezVous;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;

public class InsertRendezVousClientRequest extends ClientRequest<RendezVous, String> {

    public InsertRendezVousClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, RendezVous info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> rdvIdMap = mapper.readValue(body, Map.class);
        final String result  = rdvIdMap.get("rendezvous_idRendezVous").toString();
        return result;
    }
}
