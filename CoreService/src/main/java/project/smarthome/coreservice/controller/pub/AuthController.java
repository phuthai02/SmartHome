package project.smarthome.coreservice.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.smarthome.common.dto.request.AuthRequest;
import project.smarthome.common.dto.response.AuthResponse;
import project.smarthome.common.dto.response.ResponseAPI;
import project.smarthome.common.utils.Constants;
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

    @PostMapping("/login")
    public ResponseEntity<ResponseAPI> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails user = (UserDetails) auth.getPrincipal();

            String accessToken = jwtService.generateAccessToken(user.getUsername());
            String refreshToken = jwtService.generateRefreshToken(user.getUsername());

            AuthResponse response = new AuthResponse(
                    accessToken,
                    refreshToken,
                    user.getUsername(),
                    user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                    jwtService.getTimeToExpiration(accessToken)
            );

            return ResponseEntity.ok(ResponseAPI.success(response));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseAPI.error("Username or password is incorrect"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseAPI.error(e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseAPI> refreshToken(@RequestBody AuthRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            String username = jwtService.extractUsername(refreshToken);

            if (username == null || !jwtService.isRefreshTokenValid(refreshToken, username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseAPI.error("Invalid token"));
            }

            UserDetails user = userDetailsSecurityService.loadUserByUsername(username);
            String accessToken = jwtService.generateAccessToken(user.getUsername());

            AuthResponse response = new AuthResponse(
                    accessToken,
                    refreshToken,
                    user.getUsername(),
                    user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                    jwtService.getTimeToExpiration(accessToken)
            );

            return ResponseEntity.ok(ResponseAPI.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseAPI.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseAPI> logout(@RequestHeader("Authorization") String authHeader) {
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseAPI.error(e.getMessage()));
        }
    }
}