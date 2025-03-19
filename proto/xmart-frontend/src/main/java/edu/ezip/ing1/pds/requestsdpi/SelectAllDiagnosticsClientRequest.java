package edu.ezip.ing1.pds.requestsdpi;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Diagnostics;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class SelectAllDiagnosticsClientRequest extends ClientRequest<Object, Diagnostics> {

    public SelectAllDiagnosticsClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public Diagnostics readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Diagnostics diagnostics = mapper.readValue(body, Diagnostics.class);
        return diagnostics;
    }


}