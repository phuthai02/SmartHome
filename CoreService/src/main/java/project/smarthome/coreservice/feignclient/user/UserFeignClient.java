package project.smarthome.coreservice.feignclient.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.smarthome.common.entity.User;
import project.smarthome.coreservice.feignclient.BaseFeignClient;

@FeignClient(name = "UserFeignClient", url = "${data.service.url}/user")
public interface UserFeignClient extends BaseFeignClient<User, Long> {

    @GetMapping("/find-by-username")
    User findByUsername(@RequestParam("username") String username);

    @GetMapping("/exists-by-username")
    Boolean existsByUsername(@RequestParam("username") String username);
}
