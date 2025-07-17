package project.smarthome.adminportal.config.core;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoreServiceConfig {

    @Getter
    private static String coreServiceUrlPublic;

    @Getter
    private static String coreServiceUrlAdmin;

    @Getter
    private static String coreServiceUrlCustomer;

    @Value("${core.service.url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        coreServiceUrlPublic = baseUrl + "/pub";
        coreServiceUrlAdmin = baseUrl + "/adm";
        coreServiceUrlCustomer = baseUrl + "/cstm";
    }
}
