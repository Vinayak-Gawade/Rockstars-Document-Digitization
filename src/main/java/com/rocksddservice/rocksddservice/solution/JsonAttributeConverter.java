package com.rocksddservice.rocksddservice.solution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class JsonAttributeConverter implements AttributeConverter<String, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to String", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String digitizedData) {
        try {
            return objectMapper.readValue(digitizedData, String.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting String to JSON", e);
        }
    }
}
