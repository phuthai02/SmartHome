package project.smarthome.dataservice.controller.mysql.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.smarthome.common.entity.mysql.Home;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.mysql.home.HomeService;

@RestController
@RequestMapping("home")
public class HomeController extends BaseController<Home, Long> {

    @Autowired
    private HomeService homeService;

    @Override
    protected BaseService<Home, Long> getBaseService() {
        return homeService;
    }

    @Override
    protected String getEntityName() {
        return "HOME";
    }
}
