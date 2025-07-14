package project.smarthome.coreservice.feignclient.redis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "RedisFeignClient", url = "${dataservice.service.url}/redis")
public interface RedisFeignClient {

    @PostMapping("/set")
    Boolean set(@RequestParam("key") String key,
                @RequestParam("value") String value,
                @RequestParam(value = "timeoutInSeconds", required = false) Long timeoutInSeconds);

    @GetMapping("/get")
    String get(@RequestParam("key") String key);

    @DeleteMapping("/delete")
    Boolean delete(@RequestParam("key") String key);

    @GetMapping("/exists")
    Boolean exists(@RequestParam("key") String key);

    @PostMapping("/expire")
    Boolean expire(@RequestParam("key") String key,
                   @RequestParam("timeoutInSeconds") Long timeoutInSeconds);

    @GetMapping("/ttl")
    Long getTtl(@RequestParam("key") String key);
}