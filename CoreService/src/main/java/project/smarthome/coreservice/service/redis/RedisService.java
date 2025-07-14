package project.smarthome.coreservice.service.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.smarthome.coreservice.feignclient.redis.RedisFeignClient;

@Slf4j
@Service
public class RedisService {
    @Autowired
    private RedisFeignClient redisFeignClient;

    public boolean set(String key, String value, long timeoutInSeconds) {
        try {
            redisFeignClient.set(key, value, timeoutInSeconds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String get(String key) {
        try {
            return redisFeignClient.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delete(String key) {
        try {
            return Boolean.TRUE.equals(redisFeignClient.delete(key));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exists(String key) {
        try {
            return Boolean.TRUE.equals(redisFeignClient.exists(key));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean expire(String key, long timeoutInSeconds) {
        try {
            return Boolean.TRUE.equals(redisFeignClient.expire(key, timeoutInSeconds));
        } catch (Exception e) {
            return false;
        }
    }

    public Long getTtl(String key) {
        try {
            return redisFeignClient.getTtl(key);
        } catch (Exception e) {
            return -1L;
        }
    }
}
