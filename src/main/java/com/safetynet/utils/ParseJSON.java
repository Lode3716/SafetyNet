package com.safetynet.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;


@Log4j2
public class ParseJSON {

    ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode parseJsonObject(String nameKey, byte[] jsonData) throws IOException {

        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        JsonNode rootNode = objectMapper.readTree(jsonData);
        return rootNode.path(nameKey);

    }

}
