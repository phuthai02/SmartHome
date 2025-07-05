package project.smarthome.dataservice.controller.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import project.smarthome.common.entity.Home;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.home.HomeService;

@Controller
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
