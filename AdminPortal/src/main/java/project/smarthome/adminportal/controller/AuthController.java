package project.smarthome.adminportal.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.smarthome.adminportal.config.core.CoreServiceConfig;
import project.smarthome.adminportal.http.HTTPRequest;
import project.smarthome.common.dto.request.AuthRequest;
import project.smarthome.common.dto.response.AuthResponse;
import project.smarthome.common.dto.response.ResponseAPI;
import project.smarthome.common.utils.Constants;
import project.smarthome.common.utils.JsonUtils;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

    @PostMapping("/login")
    public void login(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        try {
            HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
            ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                    CoreServiceConfig.getCoreServiceUrlPublic() + "/auth/login",
                    null,
                    HttpMethod.POST,
                    null,
                    request
            );

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                AuthResponse authResponse = JsonUtils.cast(response.getBody().getData(), AuthResponse.class);
                httpRequest.getSession().setAttribute(Constants.ACCESS_TOKEN, authResponse.getTokenType() + authResponse.getAccessToken());
                httpRequest.getSession().setAttribute(Constants.REFRESH_TOKEN, authResponse.getRefreshToken());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest httpRequest) {
        try {
            HttpSession session = httpRequest.getSession();
            String accessToken = (String) session.getAttribute(Constants.ACCESS_TOKEN);

            if (accessToken != null) {
                HTTPRequest<ResponseAPI> httpRequestHelper = new HTTPRequest<>(ResponseAPI.class);
                ResponseEntity<ResponseAPI> response = httpRequestHelper.connect(
                        CoreServiceConfig.getCoreServiceUrlPublic() + "/auth/logout",
                        null,
                        HttpMethod.POST,
                        null,
                        null
                );
            }

            session.removeAttribute(Constants.ACCESS_TOKEN);
            session.removeAttribute(Constants.REFRESH_TOKEN);
            session.invalidate();

            return "redirect:/login?logout=true";
        } catch (Exception e) {
            log.error("Error during logout", e);
            HttpSession session = httpRequest.getSession();
            session.removeAttribute(Constants.ACCESS_TOKEN);
            session.removeAttribute(Constants.REFRESH_TOKEN);
            session.invalidate();
            return "redirect:/login?logout=true";
        }
    }
}