package edu.ezip.ing1.pds.requests;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Equipement;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class InsertEquipementClientRequest extends ClientRequest<Equipement, String> {

    public InsertEquipementClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Equipement info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> equipementIdMap = mapper.readValue(body, Map.class);
        final String result  = equipementIdMap.get("equipement_idEquipement").toString();
        return result;
    }
}
