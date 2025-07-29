package project.smarthome.coreservice.service.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.smarthome.coreservice.feignclient.redis.RedisFeignClient;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisFeignClient redisFeignClient;

    @Override
    public Boolean set(String key, String value, long timeoutInSeconds) {
        try {
            redisFeignClient.set(key, value, timeoutInSeconds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String get(String key) {
        try {
            return redisFeignClient.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Set<String> keys(String pattern) {
        try {
            return redisFeignClient.keys(pattern);
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Boolean delete(String key) {
        try {
            return Boolean.TRUE.equals(redisFeignClient.delete(key));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean exists(String key) {
        try {
            return Boolean.TRUE.equals(redisFeignClient.exists(key));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean expire(String key, long timeoutInSeconds) {
        try {
            return Boolean.TRUE.equals(redisFeignClient.expire(key, timeoutInSeconds));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Long getTtl(String key) {
        try {
            return redisFeignClient.getTtl(key);
        } catch (Exception e) {
            return -1L;
        }
    }
}
