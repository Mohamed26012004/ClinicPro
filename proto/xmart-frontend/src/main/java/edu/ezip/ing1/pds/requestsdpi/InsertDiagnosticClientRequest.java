package edu.ezip.ing1.pds.requestsdpi;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Diagnostic;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class InsertDiagnosticClientRequest extends ClientRequest<Diagnostic, String> {

    public InsertDiagnosticClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Diagnostic info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> DiagnosticIdMap = mapper.readValue(body, Map.class);
        final String result  = DiagnosticIdMap.get("Diagnostic_id_Diagnostic").toString();
        return result;
    }
}