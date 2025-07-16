package project.smarthome.coreservice.service.home;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.smarthome.coreservice.feignclient.home.HomeFeignClient;

@Slf4j
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeFeignClient homeFeignClient;
}
