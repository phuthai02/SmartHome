package project.smarthome.dataservice.service.mysql.area;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.mysql.Area;
import project.smarthome.dataservice.repository.mysql.AreaRepository;
import project.smarthome.dataservice.service.mysql.BaseMySQLService;

@Service
public class AreaService extends BaseMySQLService<Area, Long> {

    @Autowired
    private AreaRepository areaRepository;

    @Override
    protected JpaRepository<Area, Long> getRepository() {
        return areaRepository;
    }

    @Override
    protected JpaSpecificationExecutor<Area> getSpecificationExecutor() {
        return areaRepository;
    }
}
