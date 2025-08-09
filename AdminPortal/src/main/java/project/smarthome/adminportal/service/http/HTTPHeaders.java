package project.smarthome.adminportal.service.http;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import project.smarthome.common.utils.Constants;

import java.util.Objects;

public class HTTPHeaders extends HttpHeaders {

    public HTTPHeaders() {
        super();
        this.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpServletRequest req = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes()))
                .getRequest();

        String accessToken = (String) req.getSession().getAttribute(Constants.ACCESS_TOKEN);

        if (StringUtils.isNotBlank(accessToken)) {
            this.set(Constants.AUTHORIZATION, accessToken);
        }
    }
}
