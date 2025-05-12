package edu.ezip.ing1.pds.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Maintenance;
import edu.ezip.ing1.pds.business.dto.Maintenance;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;

public class InsertMaintenanceClientRequest extends ClientRequest<Maintenance, String> {
    public InsertMaintenanceClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Maintenance info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Integer> maintenanceIdMap = mapper.readValue(body, Map.class);
        final String result  = maintenanceIdMap.get("maintenance_idMaintenance").toString();
        return result;
    }
}
