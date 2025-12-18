package project.smarthome.coreservice.service.user;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.smarthome.common.dto.request.FilterRequest;
import project.smarthome.common.dto.request.PageFilterRequest;
import project.smarthome.common.dto.request.UserRequest;
import project.smarthome.common.dto.response.InformationResponse;
import project.smarthome.common.dto.response.PageFilterResponse;
import project.smarthome.common.dto.response.ResponseAPI;
import project.smarthome.common.dto.response.UserResponse;
import project.smarthome.common.entity.mysql.User;
import project.smarthome.common.utils.Constants;
import project.smarthome.common.utils.Utils;
import project.smarthome.coreservice.feignclient.user.UserFeignClient;
import project.smarthome.coreservice.service.redis.RedisService;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisService redisService;

    @Override
    public ResponseAPI getAdmins(PageFilterRequest request) {
        try {
            FilterRequest filterRequest = new FilterRequest("role", Constants.FilterOperator.EQUALS, Constants.Role.ADMIN);
            request.getFilters().add(filterRequest);
            PageFilterResponse<UserResponse> data = Utils.mapPage(userFeignClient.findByPageFilter(request), this::getUserInfo);
            InformationResponse info = getInfo(Constants.Role.ADMIN);
            Map<String, Object> response = new HashMap<>();
            response.put("data", data);
            response.put("info", info);
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
            PageFilterResponse<UserResponse> data = Utils.mapPage(userFeignClient.findByPageFilter(request), this::getUserInfo);
            InformationResponse info = getInfo(Constants.Role.CUSTOMER);
            Map<String, Object> response = new HashMap<>();
            response.put("data", data);
            response.put("info", info);
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
            user.setCreatedTime(new Timestamp(System.currentTimeMillis()));

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

        if (StringUtils.isBlank(request.getFullName())) return false;
        if (StringUtils.isBlank(request.getUsername())) return false;

        if (isCreate) {
            if (StringUtils.isBlank(request.getPassword())) return false;
            if (StringUtils.isBlank(request.getConfirmPassword())) return false;
            if (!request.getPassword().equals(request.getConfirmPassword())) return false;
            if (request.getPassword().trim().length() < 5) return false;
        } else {
            if (!StringUtils.isBlank(request.getPassword())) {
                if (StringUtils.isBlank(request.getConfirmPassword())) return false;
                if (!request.getPassword().equals(request.getConfirmPassword())) return false;
                if (request.getPassword().trim().length() < 5) return false;
            }
        }

        if (request.getUsername().trim().length() < 5) return false;

        if (!StringUtils.isBlank(request.getEmail()) && !isValidEmail(request.getEmail())) return false;

        if (!StringUtils.isBlank(request.getPhoneNumber()) && !isValidPhoneNumber(request.getPhoneNumber())) return false;

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^[+]?[0-9]{10,15}$");
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

    private InformationResponse getInfo(String role) {
        List<User> users = userFeignClient.findAll();

        List<User> usersByRole = users.stream()
                .filter(u -> role.equals(u.getRole()))
                .toList();

        int totalByRoleActive = (int) usersByRole.stream()
                .filter(u -> Constants.Status.ACTIVE.equals(u.getStatus()))
                .count();

        int totalByRoleLocked = (int) usersByRole.stream()
                .filter(u -> Constants.Status.LOCKED.equals(u.getStatus()))
                .count();

        return new InformationResponse(
                users.size(),
                usersByRole.size(),
                totalByRoleActive,
                totalByRoleLocked
        );
    }

    private void updateUserFromRequest(User user, UserRequest request) {
        if (!StringUtils.isBlank(request.getUsername())) {
            user.setUsername(request.getUsername());
        }

        if (!StringUtils.isBlank(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (!StringUtils.isBlank(request.getFullName())) {
            user.setFullName(request.getFullName());
        }

        if (request.getEmail() != null) {
            user.setEmail(StringUtils.isBlank(request.getEmail()) ? null : request.getEmail());
        }

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(StringUtils.isBlank(request.getPhoneNumber()) ? null : request.getPhoneNumber());
        }

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

    private UserResponse getUserInfo(User user) {
        UserResponse info = user.getUserInfo();
        String key = String.format(Constants.Format.ACCESS_TOKEN, user.getUsername());
        info.setOnline(redisService.exists(key));
        return info;
    }
}