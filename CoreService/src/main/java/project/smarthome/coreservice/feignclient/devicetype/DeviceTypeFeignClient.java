package project.smarthome.coreservice.feignclient.devicetype;

import org.springframework.cloud.openfeign.FeignClient;
import project.smarthome.common.entity.DeviceType;
import project.smarthome.coreservice.feignclient.BaseFeignClient;

@FeignClient(name = "DeviceTypeFeignClient", url = "${dataservice.service.url}/device-type")
public interface DeviceTypeFeignClient extends BaseFeignClient<DeviceType, Long> {
}
