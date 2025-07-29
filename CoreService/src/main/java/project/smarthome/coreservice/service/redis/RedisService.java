package project.smarthome.coreservice.service.redis;

import java.util.Set;

public interface RedisService {
    Boolean set(String key, String value, long timeoutInSeconds);
    String get(String key);
    Set<String> keys(String pattern);
    Boolean delete(String key);
    Boolean exists(String key);
    Boolean expire(String key, long timeoutInSeconds);
    Long getTtl(String key);
}
