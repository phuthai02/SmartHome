package project.smarthome.coreservice.feignclient.devicecommandhistory;

import org.springframework.cloud.openfeign.FeignClient;
import project.smarthome.common.entity.mongodb.DeviceCommandHistory;
import project.smarthome.coreservice.feignclient.BaseFeignClient;

@FeignClient(name = "DeviceCommandHistoryFeignClient", url = "${data.service.url}/device-command-history")
public interface DeviceCommandHistoryFeignClient extends BaseFeignClient<DeviceCommandHistory, String> {
}
