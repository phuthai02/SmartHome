package project.smarthome.coreservice.feignclient.home;

import org.springframework.cloud.openfeign.FeignClient;
import project.smarthome.common.entity.mysql.Home;
import project.smarthome.coreservice.feignclient.BaseFeignClient;

@FeignClient(name = "HomeFeignClient", url = "${data.service.url}/home")
public interface HomeFeignClient extends BaseFeignClient<Home, Long> {
}
