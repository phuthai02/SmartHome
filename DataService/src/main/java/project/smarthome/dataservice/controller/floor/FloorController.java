package project.smarthome.dataservice.controller.floor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import project.smarthome.common.entity.Floor;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.floor.FloorService;

@Controller
@RequestMapping("floor")
public class FloorController extends BaseController<Floor, Long> {
    @Autowired
    private FloorService floorService;

    @Override
    protected BaseService<Floor, Long> getBaseService() {
        return floorService;
    }

    @Override
    protected String getEntityName() {
        return "FLOOR";
    }
}
