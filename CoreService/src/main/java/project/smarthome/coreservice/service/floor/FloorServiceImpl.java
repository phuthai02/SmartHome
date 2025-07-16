package project.smarthome.coreservice.service.floor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.smarthome.coreservice.feignclient.floor.FloorFeignClient;

@Slf4j
@Service
public class FloorServiceImpl implements FloorService {

    @Autowired
    private FloorFeignClient floorFeignClient;
}
