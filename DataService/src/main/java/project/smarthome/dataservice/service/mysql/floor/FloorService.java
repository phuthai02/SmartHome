package project.smarthome.dataservice.service.mysql.floor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.mysql.Floor;
import project.smarthome.dataservice.repository.mysql.FloorRepository;
import project.smarthome.dataservice.service.mysql.BaseMySQLService;

@Service
public class FloorService extends BaseMySQLService<Floor, Long> {

    @Autowired
    private FloorRepository floorRepository;

    @Override
    protected JpaRepository<Floor, Long> getRepository() {
        return floorRepository;
    }

    @Override
    protected JpaSpecificationExecutor<Floor> getSpecificationExecutor() {
        return floorRepository;
    }
}
