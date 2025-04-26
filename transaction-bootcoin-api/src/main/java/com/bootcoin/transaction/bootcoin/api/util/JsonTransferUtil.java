package com.bootcoin.transaction.bootcoin.api.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JsonTransferUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // Registra el módulo para manejar Java 8 Date/Time (como LocalDate)
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static String objectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // Registrar el módulo para soportar Java 8 LocalDate y otros tipos de fecha/hora
        mapper.registerModule(new JavaTimeModule());

        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static <T> T getObjectFromJSONFile(Class<T> type, String filePath) {
        ClassLoader classLoader = type.getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T object = null;

        try {
            object = mapper.readValue(file, type);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to object: " + e.getMessage(), e);
        }
    }
}
