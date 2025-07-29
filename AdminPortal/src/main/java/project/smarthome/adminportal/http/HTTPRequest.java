package project.smarthome.adminportal.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import project.smarthome.common.utils.JsonUtils;

import java.net.URI;
import java.util.Map;

@Slf4j
public class HTTPRequest<T> {

    private final Class<T> clazz;

    public HTTPRequest(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ResponseEntity<T> connect(String url, MultiValueMap<String, String> params, HttpMethod method, Map<String, String> headers, Object body) {
        return connect(url, params, method, headers, body, null);
    }

    public ResponseEntity<T> connect(String url, MultiValueMap<String, String> params, HttpMethod method, Map<String, String> headers, Object body, MediaType mediaType) {
        try {
            return sendRequest(url, params, method, headers, body, mediaType);
        } catch (Exception e) {
            log.error("Request error: {}", e.getMessage());
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
}