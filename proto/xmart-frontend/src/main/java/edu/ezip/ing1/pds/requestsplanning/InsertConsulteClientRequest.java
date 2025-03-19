package edu.ezip.ing1.pds.requestsplanning;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Consulte;
import edu.ezip.ing1.pds.business.dto.Medecin;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;

public class InsertConsulteClientRequest extends ClientRequest<Consulte, String> {

    public InsertConsulteClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Consulte info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);

    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> medecinNumeroADELIMap = mapper.readValue(body, Map.class);
        final String result  = medecinNumeroADELIMap.get("consulte_numeroADELI").toString();
        return result;
    }
}
