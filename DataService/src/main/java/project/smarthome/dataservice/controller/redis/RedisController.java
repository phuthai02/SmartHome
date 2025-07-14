package project.smarthome.dataservice.controller.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.smarthome.dataservice.service.redis.RedisService;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/set")
    public ResponseEntity<Boolean> set(@RequestParam String key,
                                       @RequestParam String value,
                                       @RequestParam(required = false) Long timeoutInSeconds) {
        log.info("[REDIS] SET KEY={}, VALUE={}, TIMEOUT={}", key, value, timeoutInSeconds);

        boolean result;
        if (timeoutInSeconds != null) {
            result = redisService.set(key, value, timeoutInSeconds, TimeUnit.SECONDS);
        } else {
            result = redisService.set(key, value);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(@RequestParam String key) {
        log.info("[REDIS] GET KEY={}", key);

        String value = redisService.get(key);
        if (value != null) {
            return ResponseEntity.ok(value);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestParam String key) {
        log.info("[REDIS] DELETE KEY={}", key);

        boolean result = redisService.delete(key);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(@RequestParam String key) {
        log.info("[REDIS] EXISTS KEY={}", key);

        boolean exists = redisService.exists(key);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/expire")
    public ResponseEntity<Boolean> expire(@RequestParam String key,
                                          @RequestParam Long timeoutInSeconds) {
        log.info("[REDIS] EXPIRE KEY={}, TIMEOUT={}", key, timeoutInSeconds);

        boolean result = redisService.expire(key, timeoutInSeconds, TimeUnit.SECONDS);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/ttl")
    public ResponseEntity<Long> getTtl(@RequestParam String key) {
        log.info("[REDIS] TTL KEY={}", key);

        Long ttl = redisService.getExpire(key);
        return ResponseEntity.ok(ttl);
    }
}