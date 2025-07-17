package project.smarthome.adminportal.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.smarthome.adminportal.http.HTTPRequest;
import project.smarthome.common.utils.JsonUtils;

@Slf4j
@RestController
@RequestMapping("device")
public class Device {
    @GetMapping
    public String cstm(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();

        HTTPRequest<String> httpRequestHelper = new HTTPRequest<>(String.class);
        ResponseEntity<String> response = httpRequestHelper.connect(
                "http://localhost:8080/smarthome-core/cstm/device",
                null,
                HttpMethod.GET,
                null,
                null
        );

        return JsonUtils.toJson(response.getBody());
    }
}
