package edu.ezip.ing1.pds.requests;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.Maintenances;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class SelectAllMaintenancesClientRequest extends ClientRequest<Object, Maintenances> {

    public SelectAllMaintenancesClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public Maintenances readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Maintenances maintenances = mapper.readValue(body, Maintenances.class);
        return maintenances;
    }


}
