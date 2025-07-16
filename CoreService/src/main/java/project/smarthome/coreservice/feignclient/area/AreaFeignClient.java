package project.smarthome.coreservice.feignclient.area;

import org.springframework.cloud.openfeign.FeignClient;
import project.smarthome.common.entity.Area;
import project.smarthome.coreservice.feignclient.BaseFeignClient;

@FeignClient(name = "AreaFeignClient", url = "${data.service.url}/area")
public interface AreaFeignClient extends BaseFeignClient<Area, Long> {
}
