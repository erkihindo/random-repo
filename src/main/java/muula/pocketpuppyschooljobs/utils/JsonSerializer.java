package muula.pocketpuppyschooljobs.utils;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum JsonSerializer {
    JSON_SERIALIZER;

    public final ObjectMapper objectMapper;

    JsonSerializer() {

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper = new ObjectMapper().registerModule(javaTimeModule)
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(WRITE_DATES_AS_TIMESTAMPS, false)
        ;
    }

    public <T> T readValue(String json, Class classType) {
        if (json == null) return null;

        try {
            return (T) objectMapper.readValue(json, classType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> String writeAsJson(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (Exception e) {
            log.error("Unable to write value as json.", e);
            throw new RuntimeException(e);
        }
    }
}
