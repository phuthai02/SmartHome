package project.smarthome.coreservice.service.user;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.smarthome.common.dto.request.FilterRequest;
import project.smarthome.common.dto.request.PageFilterRequest;
import project.smarthome.common.dto.request.UserRequest;
import project.smarthome.common.dto.response.PageFilterResponse;
import project.smarthome.common.dto.response.ResponseAPI;
import project.smarthome.common.dto.response.UserResponse;
import project.smarthome.common.entity.mysql.User;
import project.smarthome.common.utils.Constants;
import project.smarthome.coreservice.feignclient.user.UserFeignClient;

import java.util.Base64;
import java.util.function.Function;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseAPI getAdmins(PageFilterRequest request) {
        try {
            FilterRequest filterRequest = new FilterRequest("role", Constants.FilterOperator.EQUALS, Constants.Role.ADMIN);
            request.getFilters().add(filterRequest);
            PageFilterResponse<UserResponse> response = mapPage(userFeignClient.findByPageFilter(request), User::getUserInfo);
            return ResponseAPI.success(response);
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách quản trị viên: {}", e.getMessage(), e);
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI getCustomers(PageFilterRequest request) {
        try {
            FilterRequest filterRequest = new FilterRequest("role", Constants.FilterOperator.EQUALS, Constants.Role.CUSTOMER);
            request.getFilters().add(filterRequest);
            PageFilterResponse<UserResponse> response = mapPage(userFeignClient.findByPageFilter(request), User::getUserInfo);
            return ResponseAPI.success(response);
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách khách hàng: {}", e.getMessage(), e);
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI createAdmin(UserRequest request) {
        try {
            if (!isValidUserRequest(request, true))
                return ResponseAPI.error("Dữ liệu người dùng không hợp lệ");

            if (userFeignClient.existsByUsername(request.getUsername()))
                return ResponseAPI.error("Tên đăng nhập đã tồn tại");

            User user = createUserFromRequest(request);
            user.setRole(Constants.Role.ADMIN);
            user.setStatus(Constants.Status.ACTIVE);

            User createdUser = userFeignClient.create(user);
            return createdUser != null ? ResponseAPI.success("Tạo quản trị viên thành công") : ResponseAPI.error("Tạo quản trị viên thất bại");
        } catch (Exception e) {
            log.error("Lỗi khi tạo quản trị viên: {}", e.getMessage(), e);
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI createCustomer(UserRequest request) {
        try {
            if (!isValidUserRequest(request, true))
                return ResponseAPI.error("Dữ liệu người dùng không hợp lệ");

            if (userFeignClient.existsByUsername(request.getUsername()))
                return ResponseAPI.error("Tên đăng nhập đã tồn tại");

            User user = createUserFromRequest(request);
            user.setRole(Constants.Role.CUSTOMER);
            user.setStatus(Constants.Status.ACTIVE);

            User createdUser = userFeignClient.create(user);
            return createdUser != null ? ResponseAPI.success("Tạo khách hàng thành công") : ResponseAPI.error("Tạo khách hàng thất bại");
        } catch (Exception e) {
            log.error("Lỗi khi tạo khách hàng: {}", e.getMessage(), e);
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI update(UserRequest request) {
        try {
            if (request.getId() == null)
                return ResponseAPI.error("ID người dùng là bắt buộc để cập nhật");

            if (!isValidUserRequest(request, false))
                return ResponseAPI.error("Dữ liệu người dùng không hợp lệ");

            User existingUser = userFeignClient.findById(request.getId());
            if (existingUser == null)
                return ResponseAPI.error("Không tìm thấy người dùng");

            if (!existingUser.getUsername().equals(request.getUsername()) && userFeignClient.existsByUsername(request.getUsername()))
                return ResponseAPI.error("Tên đăng nhập đã tồn tại");

            updateUserFromRequest(existingUser, request);

            User updatedUser = userFeignClient.update(request.getId(), existingUser);
            return updatedUser != null ? ResponseAPI.success("Cập nhật người dùng thành công") : ResponseAPI.error("Cập nhật người dùng thất bại");
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật người dùng: {}", e.getMessage(), e);
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI delete(Long id) {
        try {
            if (id == null)
                return ResponseAPI.error("ID người dùng là bắt buộc");

            User user = userFeignClient.findById(id);
            if (user == null)
                return ResponseAPI.error("Không tìm thấy người dùng");

            userFeignClient.delete(id);
            return ResponseAPI.success("Xóa người dùng thành công");
        } catch (Exception e) {
            log.error("Lỗi khi xóa người dùng: {}", e.getMessage(), e);
            return ResponseAPI.error();
        }
    }

    @Override
    public ResponseAPI toggleStatus(Long id) {
        try {
            if (id == null)
                return ResponseAPI.error("ID người dùng là bắt buộc");

            User user = userFeignClient.findById(id);
            if (user == null)
                return ResponseAPI.error("Không tìm thấy người dùng");

            String newStatus = Constants.Status.ACTIVE.equals(user.getStatus()) ? Constants.Status.LOCKED : Constants.Status.ACTIVE;
            user.setStatus(newStatus);

            User updatedUser = userFeignClient.update(id, user);
            if (updatedUser != null) {
                String action = Constants.Status.ACTIVE.equals(newStatus) ? "kích hoạt" : "vô hiệu hoá";
                return ResponseAPI.success("Người dùng đã được " + action + " thành công");
            }
            return ResponseAPI.error("Thay đổi trạng thái người dùng thất bại");
        } catch (Exception e) {
            log.error("Lỗi khi thay đổi trạng thái người dùng: {}", e.getMessage(), e);
            return ResponseAPI.error();
        }
    }

    private boolean isValidUserRequest(UserRequest request, boolean isCreate) {
        if (request == null) return false;

        if (isCreate) {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) return false;
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) return false;
            if (request.getConfirmPassword() == null || !request.getPassword().equals(request.getConfirmPassword()))
                return false;
        }

        if (request.getEmail() != null && !isValidEmail(request.getEmail())) return false;
        if (request.getPhoneNumber() != null && !isValidPhoneNumber(request.getPhoneNumber())) return false;

        return true;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^[+]?[0-9]{10,15}$");
    }

    private User createUserFromRequest(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        if (request.getAvatarBase64() != null && !request.getAvatarBase64().trim().isEmpty()) {
            try {
                user.setAvatar(Base64.getDecoder().decode(request.getAvatarBase64()));
            } catch (IllegalArgumentException e) {
                log.warn("Dữ liệu ảnh đại diện base64 không hợp lệ: {}", e.getMessage());
            }
        }

        return user;
    }

    private void updateUserFromRequest(User user, UserRequest request) {
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (!StringUtils.isBlank(request.getPassword())) user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        if (request.getAvatarBase64() != null) {
            if (request.getAvatarBase64().trim().isEmpty()) {
                user.setAvatar(null);
            } else {
                try {
                    user.setAvatar(Base64.getDecoder().decode(request.getAvatarBase64()));
                } catch (IllegalArgumentException e) {
                    log.warn("Dữ liệu ảnh đại diện base64 không hợp lệ khi cập nhật: {}", e.getMessage());
                }
            }
        }
    }

    private <T, R> PageFilterResponse<R> mapPage(PageFilterResponse<T> page, Function<T, R> mapper) {
        return PageFilterResponse.<R>builder()
                .content(page.getContent().stream().map(mapper).toList())
                .page(page.getPage())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}