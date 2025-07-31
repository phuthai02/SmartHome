package project.smarthome.coreservice.feignclient.alerthistory;

import org.springframework.cloud.openfeign.FeignClient;
import project.smarthome.common.entity.mongodb.AlertHistory;
import project.smarthome.coreservice.feignclient.BaseFeignClient;

@FeignClient(name = "AlertHistoryFeignClient", url = "${data.service.url}/alert-history")
public interface AlertHistoryFeignClient extends BaseFeignClient<AlertHistory, String> {
}
