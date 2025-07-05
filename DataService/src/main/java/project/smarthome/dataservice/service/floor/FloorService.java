package project.smarthome.dataservice.service.floor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.Floor;
import project.smarthome.dataservice.repository.FloorRepository;
import project.smarthome.dataservice.service.BaseService;

@Service
public class FloorService extends BaseService<Floor, Long> {
    @Autowired
    private FloorRepository floorRepository;

    @Override
    protected JpaRepository<Floor, Long> getRepository() {
        return floorRepository;
    }
}
