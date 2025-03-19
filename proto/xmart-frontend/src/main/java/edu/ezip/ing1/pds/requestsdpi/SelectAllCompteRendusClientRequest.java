package edu.ezip.ing1.pds.requestsdpi;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.CompteRendus;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class SelectAllCompteRendusClientRequest extends ClientRequest<Object, CompteRendus> {

    public SelectAllCompteRendusClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public CompteRendus readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final CompteRendus compteRendus = mapper.readValue(body, CompteRendus.class);
        return compteRendus;
    }


}