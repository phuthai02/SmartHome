package project.smarthome.common.dto.response;

import lombok.Data;

@Data
public class ResponseAPI {
    private boolean success;
    private String message;
    private Object data;

    public ResponseAPI(boolean success) {
        this.success = success;
    }

    public ResponseAPI(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseAPI(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public static ResponseAPI success(Object data) {
        return new ResponseAPI(true, data);
    }

    public static ResponseAPI success() {
        return new ResponseAPI(true);
    }

    public static ResponseAPI error(String message) {
        return new ResponseAPI(false, message);
    }
}
