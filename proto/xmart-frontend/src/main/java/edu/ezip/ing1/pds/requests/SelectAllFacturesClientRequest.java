package edu.ezip.ing1.pds.requests;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Factures;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class SelectAllFacturesClientRequest extends ClientRequest<Object, Factures> {

    public SelectAllFacturesClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public Factures readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Factures factures = mapper.readValue(body, Factures.class);
        return factures;
    }


}
