package edu.ezip.ing1.pds.requests.salle;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Salle;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;

public class InsertSalleClientRequest extends ClientRequest<Salle, String> {

    public InsertSalleClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Salle info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);

    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> salleIdMap = mapper.readValue(body, Map.class);
        final String result  = salleIdMap.get("salle_id").toString();
        return result;
    }
}
