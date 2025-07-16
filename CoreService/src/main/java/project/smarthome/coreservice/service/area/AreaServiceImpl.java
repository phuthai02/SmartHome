package project.smarthome.coreservice.service.area;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.smarthome.coreservice.feignclient.area.AreaFeignClient;

@Slf4j
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaFeignClient areaFeignClient;
}
