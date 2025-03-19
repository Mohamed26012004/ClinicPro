package edu.ezip.ing1.pds.requestsdpi;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.AntecedentMedical;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class InsertAntecedentMedicalClientRequest extends ClientRequest<AntecedentMedical, String> {

    public InsertAntecedentMedicalClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, AntecedentMedical info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> antecedentMedicalIdMap = mapper.readValue(body, Map.class);
        final String result  = antecedentMedicalIdMap.get("antecedentMedical_id_antecedentMedical").toString();
        return result;
    }
}