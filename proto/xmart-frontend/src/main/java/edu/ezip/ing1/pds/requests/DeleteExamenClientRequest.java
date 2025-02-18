package edu.ezip.ing1.pds.requests;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Examen;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class DeleteExamenClientRequest extends ClientRequest<Examen, String> {

    public DeleteExamenClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Examen info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> examenIdMap = mapper.readValue(body, Map.class);
        final String result  = examenIdMap.get("examen_id").toString();
        return result;
    }

}
