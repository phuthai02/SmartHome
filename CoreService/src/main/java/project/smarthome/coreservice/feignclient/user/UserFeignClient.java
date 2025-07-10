package project.smarthome.coreservice.feignclient.user;

import org.springframework.cloud.openfeign.FeignClient;
import project.smarthome.common.entity.User;
import project.smarthome.coreservice.feignclient.BaseFeignClient;

@FeignClient(name = "UserFeignClient", url = "${dataservice.service.url}/user")
public interface UserFeignClient extends BaseFeignClient<User, Long> {
}
