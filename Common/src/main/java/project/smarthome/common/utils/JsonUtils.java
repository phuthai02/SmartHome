package project.smarthome.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi khi chuyển object thành JSON", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi chuyển JSON thành object", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object data, Class<T> targetClass) {
        if (data == null) return null;

        if (targetClass.isInstance(data)) {
            return (T) data;
        }

        return mapper.convertValue(data, targetClass);
    }
}
