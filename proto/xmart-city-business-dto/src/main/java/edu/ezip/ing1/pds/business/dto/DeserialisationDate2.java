package edu.ezip.ing1.pds.business.dto;
import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
public class DeserialisationDate2 extends JsonDeserializer<LocalDate> {
    @Override    
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode noeud = p.getCodec().readTree(p);
        int annee = noeud.get("year").asInt();        
        int mois = noeud.get("monthValue").asInt();        
        int jour = noeud.get("dayOfMonth").asInt();        
        return LocalDate.of(annee, mois, jour);    
    }
}
 