package project.smarthome.adminportal.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.smarthome.common.dto.request.PageFilterRequest;
import project.smarthome.common.dto.request.UserRequest;
import project.smarthome.common.dto.response.ResponseAPI;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public ResponseAPI getAdmins(PageFilterRequest request) {
        try {
            return UserServiceCore.getAdmins(request);
        } catch (Exception e) {
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI getCustomers(PageFilterRequest request) {
        try {
            return UserServiceCore.getCustomers(request);
        } catch (Exception e) {
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI createAdmin(UserRequest request) {
        try {
            return UserServiceCore.createAdmin(request);
        } catch (Exception e) {
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI createCustomer(UserRequest request) {
        try {
            return UserServiceCore.createCustomer(request);
        } catch (Exception e) {
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI updateUser(Long id, UserRequest request) {
        try {
            return UserServiceCore.updateUser(id, request);
        } catch (Exception e) {
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI deleteUser(Long id) {
        try {
            return UserServiceCore.deleteUser(id);
        } catch (Exception e) {
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI toggleUserStatus(Long id) {
        try {
            return UserServiceCore.toggleUserStatus(id);
        } catch (Exception e) {
            return ResponseAPI.error();
        }
    }
}