package project.smarthome.adminportal.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import project.smarthome.adminportal.config.core.CoreServiceConfig;
import project.smarthome.common.dto.request.AuthRequest;
import project.smarthome.common.dto.response.AuthResponse;
import project.smarthome.common.dto.response.ResponseAPI;
import project.smarthome.common.utils.Constants;
import project.smarthome.common.utils.JsonUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Slf4j
public class HTTPRequest<T> {

    private final Class<T> clazz;
    private static final int MAX_RETRY_ATTEMPTS = 1;

    public HTTPRequest(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ResponseEntity<T> connect(String url, MultiValueMap<String, String> params, HttpMethod method, Map<String, String> headers, Object body) {
        return connect(url, params, method, headers, body, null);
    }

    public ResponseEntity<T> connect(String url, MultiValueMap<String, String> params, HttpMethod method, Map<String, String> headers, Object body, MediaType mediaType) {
        return connectRetry(url, params, method, headers, body, mediaType, 0);
    }

    private ResponseEntity<T> connectRetry(String url, MultiValueMap<String, String> params, HttpMethod method, Map<String, String> headers, Object body, MediaType mediaType, int retryCount) {
        try {
            return sendRequest(url, params, method, headers, body, mediaType);
        } catch (Exception e) {
            log.error("Request error: {}", e.getMessage());

            if (shouldRetry(e, url, retryCount) && refreshToken()) {
                return connectRetry(url, params, method, headers, body, mediaType, retryCount + 1);
            }

            sendError();
        }
        return null;
    }

    private ResponseEntity<T> connectNoRetry(String url, MultiValueMap<String, String> params, HttpMethod method, Map<String, String> headers, Object body, MediaType mediaType) {
        try {
            return sendRequest(url, params, method, headers, body, mediaType);
        } catch (Exception e) {
            log.error("Request error (no retry): {}", e.getMessage());
        }
        return null;
    }

    private ResponseEntity<T> sendRequest(String url, MultiValueMap<String, String> params, HttpMethod method, Map<String, String> headers, Object body, MediaType mediaType) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build().encode().toUri();

        HTTPHeaders httpHeaders = new HTTPHeaders();
        if (mediaType != null) httpHeaders.setContentType(mediaType);
        if (headers != null) headers.forEach(httpHeaders::set);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        log.info("HTTP {} {}", method, uri);
        if (body != null) log.info("Request body: {}", JsonUtils.toJson(body));

        ResponseEntity<T> response = new RestTemplate().exchange(uri, method, request, clazz);

        log.info("Response status: {}", response.getStatusCode());
        log.info("Response body  : {}", JsonUtils.toJson(response.getBody()));

        return response;
    }

    private boolean shouldRetry(Exception e, String url, int retryCount) {
        String msg = e.getMessage();
        if (msg != null && (msg.contains("401") || msg.contains("Unauthorized"))) {
            return retryCount < MAX_RETRY_ATTEMPTS && !url.startsWith(CoreServiceConfig.getCoreServiceUrlPublic());
        }
        return false;
    }

    private boolean refreshToken() {
        try {
            HttpServletRequest request = getCurrentRequest();
            if (request == null) return false;

            HttpSession session = request.getSession();
            String refreshToken = (String) session.getAttribute(Constants.REFRESH_TOKEN);
            if (refreshToken == null) return false;

            AuthRequest authRequest = new AuthRequest();
            authRequest.setRefreshToken(refreshToken);

            HTTPRequest<ResponseAPI> refreshHelper = new HTTPRequest<>(ResponseAPI.class);
            ResponseEntity<ResponseAPI> response = refreshHelper.connectNoRetry(CoreServiceConfig.getCoreServiceUrlPublic() + "/auth/refresh", null, HttpMethod.POST, null, authRequest, null);

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

    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attr != null ? attr.getRequest() : null;
        } catch (Exception e) {
            log.error("Could not get current request", e);
            return null;
        }
    }

    private HttpServletResponse getCurrentResponse() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attr != null ? attr.getResponse() : null;
        } catch (Exception e) {
            log.error("Could not get current response", e);
            return null;
        }
    }

    private void sendError() {
        HttpServletResponse response = getCurrentResponse();
        if (response != null) {
            try {
                clearSession();

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                String error = JsonUtils.toJson(ResponseAPI.error("Authentication failed"));
                response.getWriter().write(error);
                response.getWriter().flush();

                throw new RuntimeException("Authentication failed");
            } catch (IOException e) {
                log.error("Error writing unauthorized response", e);
            }
        }
    }

    private void clearSession() {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            HttpSession session = request.getSession();
            session.removeAttribute(Constants.ACCESS_TOKEN);
            session.removeAttribute(Constants.REFRESH_TOKEN);
        }
    }
}
