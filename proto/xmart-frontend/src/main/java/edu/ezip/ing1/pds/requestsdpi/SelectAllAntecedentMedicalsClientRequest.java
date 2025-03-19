package edu.ezip.ing1.pds.requestsdpi;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.business.dto.AntecedentMedicals;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class SelectAllAntecedentMedicalsClientRequest extends ClientRequest<Object, AntecedentMedicals> {

    public SelectAllAntecedentMedicalsClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes) throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public AntecedentMedicals readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final AntecedentMedicals antecedentMedicals = mapper.readValue(body, AntecedentMedicals.class);
        return antecedentMedicals;
    }


}