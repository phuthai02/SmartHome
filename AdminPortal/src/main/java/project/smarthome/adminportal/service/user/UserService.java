package project.smarthome.adminportal.service.user;

import project.smarthome.common.dto.request.PageFilterRequest;
import project.smarthome.common.dto.request.UserRequest;
import project.smarthome.common.dto.response.ResponseAPI;

public interface UserService {
    ResponseAPI getAdmins(PageFilterRequest request);
    ResponseAPI getCustomers(PageFilterRequest request);
    ResponseAPI createAdmin(UserRequest request);
    ResponseAPI createCustomer(UserRequest request);
    ResponseAPI updateUser(Long id, UserRequest request);
    ResponseAPI deleteUser(Long id);
    ResponseAPI toggleUserStatus(Long id);
}
