package project.smarthome.coreservice.config.web;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class PackagePrefixHandlerMapping extends RequestMappingHandlerMapping {

    private final String basePackage;

    public PackagePrefixHandlerMapping(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo methodMapping = super.getMappingForMethod(method, handlerType);
        if (methodMapping == null) return null;

        String packageName = handlerType.getPackageName();

        if (!packageName.startsWith(basePackage)) {
            return methodMapping;
        }

        String subPackage = packageName.replace(basePackage + ".", "").split("\\.")[0];
        String prefix = "/" + subPackage;

        RequestMappingInfo prefixInfo = RequestMappingInfo.paths(prefix).build();

        return prefixInfo.combine(methodMapping);
    }
}
