package org.nh.artha.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Created by vagrant on 2/6/17.
 */
public class ValueSetCodeDeserializer extends JsonDeserializer<String>{

    /**
     * It converts given JSON object to Long type
     * @param jsonParser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return node.path("code").isMissingNode() ? (node.asText().isEmpty() ? null : node.asText()) : node.get("code").asText();
    }
}
