package edu.ezip.ing1.pds.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Facture;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;

public class InsertFacturesClientRequest extends ClientRequest<Facture, String> {

    public InsertFacturesClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Facture info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> factureIdFactureMap = mapper.readValue(body, Map.class);
        final String result  = factureIdFactureMap.get("idFacture").toString();
        return result;
    }
}
