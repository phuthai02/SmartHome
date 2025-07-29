package project.smarthome.dataservice.controller.area;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.smarthome.common.entity.mysql.Area;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.mysql.BaseMySQLService;
import project.smarthome.dataservice.service.mysql.area.AreaService;

@RestController
@RequestMapping("area")
public class AreaController extends BaseController<Area, Long> {

    @Autowired
    private AreaService areaService;

    @Override
    protected BaseMySQLService<Area, Long> getBaseService() {
        return areaService;
    }

    @Override
    protected String getEntityName() {
        return "AREA";
    }
}
