package webapi.infrastructure.helper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        LocalDateTimeDeserializer localDateTimeDeserializer =
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        MAPPER.registerModule(
                new JavaTimeModule().addDeserializer(LocalDateTime.class, localDateTimeDeserializer));
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }

    public static <T> T map(String json, TypeReference<T> valueTypeRef)
            throws JsonProcessingException {
        return MAPPER.readValue(json, valueTypeRef);
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return MAPPER.readValue(json, clazz);
    }
    public static Map<String, Object> convertJsonToList(String json) {
        try {
            return MAPPER.readValue(json, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to List", e);
        }
    }
}
