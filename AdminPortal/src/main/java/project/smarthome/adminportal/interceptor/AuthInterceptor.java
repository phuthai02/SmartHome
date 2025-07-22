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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/smarthome-admin/login")) {
            HttpSession session = request.getSession(false);
            String accessToken = session != null ? (String) session.getAttribute(Constants.ACCESS_TOKEN) : null;

            if (accessToken != null && !accessToken.isEmpty() && validateTokenWithRefresh(accessToken, session)) {
                response.sendRedirect("/smarthome-admin/dashboard");
                return false;
            } else {
                if (session != null) clearSession(session);
                return true;
            }
        }

        HttpSession session = request.getSession(false);
        if (session == null) return redirectLogin(response, 403);

        String accessToken = (String) session.getAttribute(Constants.ACCESS_TOKEN);
        if (accessToken == null || accessToken.isEmpty()) return redirectLogin(response, 403);

        if (!validateTokenWithRefresh(accessToken, session)) {
            clearSession(session);
            return redirectLogin(response, 403);
        }

        return true;
    }

    private boolean validateTokenWithRefresh(String accessToken, HttpSession session) {
        if (validateToken(accessToken)) {
            return true;
        }
        return refreshToken(session);
    }

    private boolean validateToken(String accessToken) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(Constants.AUTHORIZATION, accessToken);

            HTTPRequest<ResponseAPI> httpRequest = new HTTPRequest<>(ResponseAPI.class);
            ResponseEntity<ResponseAPI> response = httpRequest.connect(
                    CoreServiceConfig.getCoreServiceUrlPublic() + "/auth/validate",
                    null,
                    HttpMethod.GET,
                    headers,
                    null
            );

            return response != null && response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            return false;
        }
    }

    private boolean refreshToken(HttpSession session) {
        try {
            String refreshToken = (String) session.getAttribute(Constants.REFRESH_TOKEN);
            if (refreshToken == null || refreshToken.isEmpty())  return false;

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

            if (response == null || !response.getStatusCode().is2xxSuccessful()) return false;

            AuthResponse authResponse = JsonUtils.cast(Objects.requireNonNull(response.getBody()).getData(), AuthResponse.class);
            session.setAttribute(Constants.ACCESS_TOKEN, authResponse.getTokenType() + authResponse.getAccessToken());
            session.setAttribute(Constants.REFRESH_TOKEN, authResponse.getRefreshToken());
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private void clearSession(HttpSession session) {
        session.removeAttribute(Constants.ACCESS_TOKEN);
        session.removeAttribute(Constants.REFRESH_TOKEN);
    }

    private boolean redirectLogin(HttpServletResponse response, Integer errorCode) throws IOException {
        response.sendRedirect("/smarthome-admin/login?error=" + errorCode);
        return false;
    }
}