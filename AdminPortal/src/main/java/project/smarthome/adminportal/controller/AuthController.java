package project.smarthome.adminportal.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.smarthome.adminportal.config.core.CoreServiceConfig;
import project.smarthome.adminportal.http.HTTPRequest;
import project.smarthome.common.dto.request.AuthRequest;
import project.smarthome.common.dto.response.AuthResponse;
import project.smarthome.common.dto.response.ResponseAPI;
import project.smarthome.common.utils.Constants;
import project.smarthome.common.utils.JsonUtils;

@Slf4j
@Controller
@RequestMapping("auth")
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletRequest httpRequest) {
        try {
            AuthRequest request = new AuthRequest();
            request.setUsername(username);
            request.setPassword(password);
            request.setClientType(Constants.ClientType.ADMIN);
            HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
            ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                    CoreServiceConfig.getCoreApiServiceUrl() + "/auth/login",
                    null,
                    HttpMethod.POST,
                    null,
                    request
            );
            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                AuthResponse authResponse = JsonUtils.cast(response.getBody().getData(), AuthResponse.class);
                httpRequest.getSession().setAttribute(Constants.ACCESS_TOKEN, authResponse.getTokenType() + authResponse.getAccessToken());
                httpRequest.getSession().setAttribute(Constants.REFRESH_TOKEN, authResponse.getRefreshToken());
                httpRequest.getSession().setAttribute(Constants.USER_INFO, authResponse.getUserInfo());
                return "redirect:/dashboard";
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "redirect:/login?error=401";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String accessToken = (String) session.getAttribute(Constants.ACCESS_TOKEN);

            if (accessToken != null) {
                try {
                    new HTTPRequest<>(ResponseAPI.class).connect(
                            CoreServiceConfig.getCoreApiServiceUrl() + "/auth/logout",
                            null,
                            HttpMethod.POST,
                            null,
                            null
                    );
                } catch (Exception e) {
                    log.error("Error during logout request", e);
                }
            }

            session.removeAttribute(Constants.ACCESS_TOKEN);
            session.removeAttribute(Constants.REFRESH_TOKEN);
            session.removeAttribute(Constants.USER_INFO);
            session.invalidate();
        }
        return "redirect:/login?logout=true";
    }
}