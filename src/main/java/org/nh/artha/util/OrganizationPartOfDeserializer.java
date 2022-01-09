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
public class OrganizationPartOfDeserializer extends JsonDeserializer<Long> {

    /**
     * It converts given json to String type
     * @param jsonParser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return node.isMissingNode() ? null : (node.path("id").isMissingNode() ? node.asLong() : node.get("id").asLong());
    }
}
