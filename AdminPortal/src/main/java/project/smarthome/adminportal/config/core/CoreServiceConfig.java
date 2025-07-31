package project.smarthome.adminportal.config.core;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoreServiceConfig {

    @Getter
    private static String coreApiServiceUrl;

    @Getter
    private static String coreAdminServiceUrl;

    @Getter
    private static String coreCustomerServiceUrl;

    @Value("${core.service.url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        coreApiServiceUrl = baseUrl + "/api";
        coreAdminServiceUrl = baseUrl + "/adm";
        coreCustomerServiceUrl = baseUrl + "/cstm";
    }
}
