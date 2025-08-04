package project.smarthome.coreservice.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.smarthome.common.dto.request.AuthRequest;
import project.smarthome.common.dto.response.AuthResponse;
import project.smarthome.common.dto.response.ResponseAPI;
import project.smarthome.common.utils.Constants;
import project.smarthome.common.utils.JsonUtils;
import project.smarthome.coreservice.model.UserDetailsSecurity;
import project.smarthome.coreservice.service.jwt.JwtService;
import project.smarthome.coreservice.service.user.UserDetailsSecurityService;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsSecurityService userDetailsSecurityService;

    private final String prefixLog = "[AUTH]";

    @PostMapping("/login")
    public ResponseEntity<ResponseAPI> login(@RequestBody AuthRequest request) {
        log.info("{} Login request: {}", prefixLog, JsonUtils.toJson(request));
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetailsSecurity user = (UserDetailsSecurity) auth.getPrincipal();

            boolean isAdminRequest = Constants.ClientType.ADMIN.equals(request.getClientType());
            boolean isNotAdminUser = user.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(Constants.Role.ADMIN));
            if (isAdminRequest && isNotAdminUser) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseAPI.error("User do not have permission"));
            }

            String accessToken = jwtService.generateAccessToken(user.getUsername());
            String refreshToken = jwtService.generateRefreshToken(user.getUsername());

            AuthResponse response = new AuthResponse(
                    user.getUserInfo(),
                    accessToken,
                    refreshToken,
                    jwtService.getTimeToExpiration(accessToken)
            );

            return ResponseEntity.ok(ResponseAPI.success(response));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseAPI.error("Username or password is incorrect"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseAPI.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseAPI> logout(@RequestHeader("Authorization") String authHeader) {
        log.info("{} Logout request: {}", prefixLog, authHeader);
        try {
            if (authHeader == null || !authHeader.startsWith(Constants.JWT_BEARER)) {
                return ResponseEntity.badRequest().body(ResponseAPI.error("Invalid header"));
            }

            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            if (username != null) {
                jwtService.revokeAccessToken(username);
                jwtService.revokeRefreshToken(username);
            }

            return ResponseEntity.ok(ResponseAPI.success("Logout successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseAPI.error(e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<ResponseAPI> validateToken(@RequestHeader("Authorization") String authHeader) {
        log.info("{} Validate request: {}", prefixLog, authHeader);
        try {
            if (authHeader == null || !authHeader.startsWith(Constants.JWT_BEARER)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseAPI.error("Invalid header"));
            }

            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            if (username != null && jwtService.isAccessTokenValid(token, username)) {
                return ResponseEntity.ok(ResponseAPI.success("Token valid"));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseAPI.error("Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseAPI.error(e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseAPI> refreshToken(@RequestBody AuthRequest request) {
        log.info("{} Refresh request: {}", prefixLog, request);
        try {
            String refreshToken = request.getRefreshToken();
            String username = jwtService.extractUsername(refreshToken);

            if (username == null || !jwtService.isRefreshTokenValid(refreshToken, username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseAPI.error("Invalid token"));
            }

            UserDetailsSecurity user = (UserDetailsSecurity) userDetailsSecurityService.loadUserByUsername(username);
            String accessToken = jwtService.generateAccessToken(user.getUsername());

            AuthResponse response = new AuthResponse(
                    user.getUserInfo(),
                    accessToken,
                    refreshToken,
                    jwtService.getTimeToExpiration(accessToken)
            );

            return ResponseEntity.ok(ResponseAPI.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseAPI.error(e.getMessage()));
        }
    }
}