package project.smarthome.coreservice.feignclient.floor;

import org.springframework.cloud.openfeign.FeignClient;
import project.smarthome.common.entity.mysql.Floor;
import project.smarthome.coreservice.feignclient.BaseFeignClient;

@FeignClient(name = "FloorFeignClient", url = "${data.service.url}/floor")
public interface FloorFeignClient extends BaseFeignClient<Floor, Long> {
}
