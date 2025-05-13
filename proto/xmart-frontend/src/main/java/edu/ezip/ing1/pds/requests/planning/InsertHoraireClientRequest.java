package edu.ezip.ing1.pds.requests.planning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Horaire;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;

public class InsertHoraireClientRequest extends ClientRequest<Horaire, String> {

    public InsertHoraireClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Horaire info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);

    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> horaireIdMap = mapper.readValue(body, Map.class);
        final String result  = horaireIdMap.get("horaire_id").toString();
        return result;
    }
}
