package project.smarthome.dataservice.controller.area;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import project.smarthome.common.entity.Area;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.area.AreaService;

@Controller
@RequestMapping("area")
public class AreaController extends BaseController<Area, Long> {
    @Autowired
    private AreaService areaService;

    @Override
    protected BaseService<Area, Long> getBaseService() {
        return areaService;
    }

    @Override
    protected String getEntityName() {
        return "AREA";
    }
}
