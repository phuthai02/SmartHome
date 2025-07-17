package project.smarthome.adminportal.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.smarthome.adminportal.config.core.CoreServiceConfig;
import project.smarthome.adminportal.http.HTTPRequest;
import project.smarthome.common.dto.request.AuthRequest;
import project.smarthome.common.dto.response.AuthResponse;
import project.smarthome.common.dto.response.ResponseAPI;
import project.smarthome.common.utils.Constants;
import project.smarthome.common.utils.JsonUtils;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        log.info("requestURI: {}", requestURI);

        if (session == null) {
            response.sendRedirect("/smarthome-admin/login");
        }

        if (requestURI.equals("/smarthome-admin/login")) {
            if (session != null) {
                String accessToken = (String) session.getAttribute(Constants.ACCESS_TOKEN);
                if (accessToken != null && !accessToken.isEmpty()) {
                    if (isTokenValid()) {
                        response.sendRedirect("/smarthome-admin/dashboard");
                        return false;
                    } else {
                        clearSession(session);
                    }
                }
            }
            return true;
        }

        if (requestURI.startsWith("/smarthome-admin/css/") ||
                requestURI.startsWith("/smarthome-admin/js/") ||
                requestURI.startsWith("/smarthome-admin/images/") ||
                requestURI.startsWith("/smarthome-admin/static/")) {
            return true;
        }

        // Check login cho các request khác
        if (session == null) {
            response.sendRedirect("/smarthome-admin/login?error=1");
            return false;
        }

        String accessToken = (String) session.getAttribute(Constants.ACCESS_TOKEN);
        if (accessToken == null || accessToken.isEmpty()) {
            response.sendRedirect("/smarthome-admin/login?error=1");
            return false;
        }

        // Check token hợp lệ
        if (!isTokenValid()) {
            if (refreshToken(session)) {
                return true;
            } else {
                clearSession(session);
                response.sendRedirect("/smarthome-admin/login?error=2");
                return false;
            }
        }

        return true;
    }

    private boolean isTokenValid() {
        try {
            HTTPRequest<ResponseAPI> httpRequest = new HTTPRequest<>(ResponseAPI.class);
            ResponseEntity<ResponseAPI> response = httpRequest.connect(
                    CoreServiceConfig.getCoreServiceUrlPublic() + "/auth/validate",
                    null,
                    HttpMethod.GET,
                    null,
                    null
            );

            return response != null && response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error validating token with core service", e);
            return false;
        }
    }

    private boolean refreshToken(HttpSession session) {
        try {
            String refreshToken = (String) session.getAttribute(Constants.REFRESH_TOKEN);
            if (refreshToken == null || refreshToken.isEmpty()) {
                return false;
            }

            AuthRequest authRequest = new AuthRequest();
            authRequest.setRefreshToken(refreshToken);

            HTTPRequest<ResponseAPI> httpRequest = new HTTPRequest<>(ResponseAPI.class);
            ResponseEntity<ResponseAPI> response = httpRequest.connect(
                    CoreServiceConfig.getCoreServiceUrlPublic() + "/auth/refresh",
                    null,
                    HttpMethod.POST,
                    null,
                    authRequest
            );

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                AuthResponse authResponse = JsonUtils.cast(response.getBody().getData(), AuthResponse.class);
                session.setAttribute(Constants.ACCESS_TOKEN, authResponse.getTokenType() + authResponse.getAccessToken());
                session.setAttribute(Constants.REFRESH_TOKEN, authResponse.getRefreshToken());
                return true;
            }
        } catch (Exception e) {
            log.error("Error during token refresh", e);
        }
        return false;
    }

    private void clearSession(HttpSession session) {
        session.removeAttribute(Constants.ACCESS_TOKEN);
        session.removeAttribute(Constants.REFRESH_TOKEN);
    }
}