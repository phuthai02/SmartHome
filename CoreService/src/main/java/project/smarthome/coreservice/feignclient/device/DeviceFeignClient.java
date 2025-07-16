package project.smarthome.coreservice.feignclient.device;

import org.springframework.cloud.openfeign.FeignClient;
import project.smarthome.common.entity.Device;
import project.smarthome.coreservice.feignclient.BaseFeignClient;

@FeignClient(name = "DeviceFeignClient", url = "${data.service.url}/device")
public interface DeviceFeignClient extends BaseFeignClient<Device, Long> {
}
