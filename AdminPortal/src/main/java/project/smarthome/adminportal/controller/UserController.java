package project.smarthome.adminportal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.smarthome.adminportal.service.user.UserService;
import project.smarthome.common.dto.request.PageFilterRequest;
import project.smarthome.common.dto.request.UserRequest;
import project.smarthome.common.dto.response.ResponseAPI;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    private final String prefixLog = "[USER]";

    @GetMapping("/admins")
    public ResponseEntity<ResponseAPI> getAdmins(@ModelAttribute PageFilterRequest request) {
        log.info("{} Get admins", prefixLog);
        ResponseAPI response = userService.getAdmins(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers")
    public ResponseEntity<ResponseAPI> getCustomers(@ModelAttribute PageFilterRequest request) {
        log.info("{} Get customers", prefixLog);
        ResponseAPI response = userService.getCustomers(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    public ResponseEntity<ResponseAPI> createAdmin(@RequestBody UserRequest request) {
        log.info("{} Create admin", prefixLog);
        ResponseAPI response = userService.createAdmin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customer")
    public ResponseEntity<ResponseAPI> createCustomer(@RequestBody UserRequest request) {
        log.info("{} Create customer", prefixLog);
        ResponseAPI response = userService.createCustomer(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPI> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        log.info("{} Update user", prefixLog);
        ResponseAPI response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseAPI> deleteUser(@PathVariable Long id) {
        log.info("{} Delete user", prefixLog);
        ResponseAPI response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseAPI> toggleUserStatus(@PathVariable Long id) {
        log.info("{} Toggle user status", prefixLog);
        ResponseAPI response = userService.toggleUserStatus(id);
        return ResponseEntity.ok(response);
    }
}