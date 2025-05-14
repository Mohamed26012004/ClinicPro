package edu.ezip.ing1.pds.requests.examen;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Examens;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class SelectAllExamensClientRequest extends ClientRequest<Object, Examens> {

    public SelectAllExamensClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public Examens readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Examens examens = mapper.readValue(body, Examens.class);
        return examens;
    }

    
}
