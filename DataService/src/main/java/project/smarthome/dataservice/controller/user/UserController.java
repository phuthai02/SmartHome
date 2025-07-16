package project.smarthome.dataservice.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import project.smarthome.common.entity.User;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.user.UserService;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController extends BaseController<User, Long> {

    @Autowired
    private UserService userService;

    @Override
    protected BaseService<User, Long> getBaseService() {
        return userService;
    }

    @Override
    protected String getEntityName() {
        return "USER";
    }

    @GetMapping("/find-by-username")
    public ResponseEntity<User> findByUsername(@RequestParam("username") String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/exists-by-username")
    public ResponseEntity<Boolean> existsByUsername(@RequestParam("username") String username) {
        log.info("{} CHECK EXISTS {} BY USERNAME: {}", getLogPrefix(), getEntityName().toUpperCase(), username);
        Boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
}
