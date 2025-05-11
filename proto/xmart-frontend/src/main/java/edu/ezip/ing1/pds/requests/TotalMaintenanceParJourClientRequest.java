package edu.ezip.ing1.pds.requests;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.TotalCouts;
import edu.ezip.ing1.pds.business.dto.TotalMaintenances;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class TotalMaintenanceParJourClientRequest extends ClientRequest<Object, TotalMaintenances> {
    public TotalMaintenanceParJourClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public TotalMaintenances readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();  // Active automatiquement le support pour LocalDate
        return mapper.readValue(body, TotalMaintenances.class);
    }
}
