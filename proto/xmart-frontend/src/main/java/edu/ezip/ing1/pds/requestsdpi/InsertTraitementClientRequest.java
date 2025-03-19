package edu.ezip.ing1.pds.requestsdpi;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Traitement;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class InsertTraitementClientRequest extends ClientRequest<Traitement, String> {

    public InsertTraitementClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Traitement info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> TraitementIdMap = mapper.readValue(body, Map.class);
        final String result  = TraitementIdMap.get("Traitement_id_Traitement").toString();
        return result;
    }
}
