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


    /*public List<Persons> analysePersonnJSON(JSONArray array) {
        List<Persons> listPersons = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            String firstName = array.getJSONObject(i).getString("firstName");
            String lastName = array.getJSONObject(i).getString("lastName");
            String address = array.getJSONObject(i).getString("address");
            String city = array.getJSONObject(i).getString("city");
            String zip = array.getJSONObject(i).getString("zip");
            String phone = array.getJSONObject(i).getString("phone");
            String email = array.getJSONObject(i).getString("email");
            listPersons.add(Persons
                    .builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .zip(zip)
                    .email(email)
                    .city(city)
                    .address(address)
                    .build());
        }
        return listPersons;
    }*/

    public JsonNode parseJsonObject(String nameKey, byte[] jsonData) throws IOException {

        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        JsonNode rootNode = objectMapper.readTree(jsonData);
        return rootNode.path(nameKey);

    }

    public void updateJsonObject(String nameKey, byte[] jsonData, Object value) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            ((ObjectNode) rootNode).putObject(String.valueOf(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteJsonObject(String nameKey, byte[] jsonData, String value) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            ((ObjectNode) rootNode).put(nameKey, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
