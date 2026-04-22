package com.twobingo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Utility class for JSON serialization and pretty-printing using GSON
 */
public class JsonUtil {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    private static final Gson gsonCompact = new Gson();

    /**
     * Convert object to pretty-printed JSON string
     *
     * @param obj Object to serialize
     * @return Pretty-printed JSON string
     */
    public static String toPrettyJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Convert object to compact JSON string
     *
     * @param obj Object to serialize
     * @return Compact JSON string
     */
    public static String toJson(Object obj) {
        return gsonCompact.toJson(obj);
    }

    /**
     * Parse JSON string and pretty-print it
     *
     * @param jsonString Raw JSON string
     * @return Pretty-printed JSON string
     */
    public static String prettyPrint(String jsonString) {
        try {
            JsonElement je = JsonParser.parseString(jsonString);
            return gson.toJson(je);
        } catch (Exception e) {
            return jsonString;
        }
    }

    /**
     * Convert JSON string to object of specified type
     *
     * @param jsonString JSON string
     * @param classOfT   Target class
     * @param <T>        Type parameter
     * @return Deserialized object
     */
    public static <T> T fromJson(String jsonString, Class<T> classOfT) {
        return gson.fromJson(jsonString, classOfT);
    }
}
