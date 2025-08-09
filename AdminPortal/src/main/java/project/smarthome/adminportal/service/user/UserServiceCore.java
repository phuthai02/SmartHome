package project.smarthome.adminportal.service.user;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import project.smarthome.adminportal.config.core.CoreServiceConfig;
import project.smarthome.adminportal.service.http.HTTPRequest;
import project.smarthome.common.dto.request.PageFilterRequest;
import project.smarthome.common.dto.request.UserRequest;
import project.smarthome.common.dto.response.ResponseAPI;

public class UserServiceCore {

    public static ResponseAPI getAdmins(PageFilterRequest request) {
        HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
        ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                CoreServiceConfig.getCoreAdminServiceUrl() + "/user/admins",
                null,
                HttpMethod.POST,
                null,
                request
        );
        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return ResponseAPI.error("Không thể lấy danh sách quản trị viên");
    }

    public static ResponseAPI getCustomers(PageFilterRequest request) {
        HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
        ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                CoreServiceConfig.getCoreAdminServiceUrl() + "/user/customers",
                null,
                HttpMethod.POST,
                null,
                request
        );
        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return ResponseAPI.error("Không thể lấy danh sách khách hàng");
    }

    public static ResponseAPI createAdmin(UserRequest request) {
        HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
        ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                CoreServiceConfig.getCoreAdminServiceUrl() + "/user/admin",
                null,
                HttpMethod.POST,
                null,
                request
        );
        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return ResponseAPI.error("Không thể tạo quản trị viên");
    }

    public static ResponseAPI createCustomer(UserRequest request) {
        HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
        ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                CoreServiceConfig.getCoreAdminServiceUrl() + "/user/customer",
                null,
                HttpMethod.POST,
                null,
                request
        );
        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return ResponseAPI.error("Không thể tạo khách hàng");
    }

    public static ResponseAPI updateUser(Long id, UserRequest request) {
        HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
        ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                CoreServiceConfig.getCoreAdminServiceUrl() + "/user/" + id,
                null,
                HttpMethod.PUT,
                null,
                request
        );
        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return ResponseAPI.error("Không thể cập nhật người dùng");
    }

    public static ResponseAPI deleteUser(Long id) {
        HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
        ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                CoreServiceConfig.getCoreAdminServiceUrl() + "/user/" + id,
                null,
                HttpMethod.DELETE,
                null,
                null
        );
        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return ResponseAPI.error("Không thể xóa người dùng");
    }

    public static ResponseAPI toggleUserStatus(Long id) {
        HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
        ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                CoreServiceConfig.getCoreAdminServiceUrl() + "/user/" + id,
                null,
                HttpMethod.POST,
                null,
                null
        );
        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return ResponseAPI.error("Không thể thay đổi trạng thái người dùng");
    }
}