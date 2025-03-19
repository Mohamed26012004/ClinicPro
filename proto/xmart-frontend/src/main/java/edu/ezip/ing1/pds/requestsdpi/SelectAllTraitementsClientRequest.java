package edu.ezip.ing1.pds.requestsdpi;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Traitements;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class SelectAllTraitementsClientRequest extends ClientRequest<Object, Traitements> {

    public SelectAllTraitementsClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public Traitements readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Traitements traitements = mapper.readValue(body, Traitements.class);
        return traitements;
    }


}