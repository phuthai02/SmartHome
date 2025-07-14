package project.smarthome.dataservice.service.area;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.Area;
import project.smarthome.dataservice.repository.AreaRepository;
import project.smarthome.dataservice.service.BaseService;

@Service
public class AreaService extends BaseService<Area, Long> {

    @Autowired
    private AreaRepository areaRepository;

    @Override
    protected JpaRepository<Area, Long> getRepository() {
        return areaRepository;
    }
}
