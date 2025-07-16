package project.smarthome.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAPI {
    private boolean success;
    private String message;
    private Object data;

    private ResponseAPI(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    private ResponseAPI(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public static ResponseAPI success(Object data) {
        return new ResponseAPI(true, data);
    }

    public static ResponseAPI success(String message) {
        return new ResponseAPI(true, message);
    }

    public static ResponseAPI error(String message) {
        return new ResponseAPI(false, message);
    }
}
