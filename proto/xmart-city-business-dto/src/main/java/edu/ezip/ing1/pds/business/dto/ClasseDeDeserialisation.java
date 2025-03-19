package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalTime;

public class ClasseDeDeserialisation extends JsonDeserializer<LocalTime> {


    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        int heure = node.get("hour").asInt();
        int minute = node.get("minute").asInt();
//        int second = node.get("second").asInt();
//        int nano = node.get("nano").asInt();

        // Créez un LocalTime avec les valeurs extraites
        return LocalTime.of(heure, minute);
    }


}
