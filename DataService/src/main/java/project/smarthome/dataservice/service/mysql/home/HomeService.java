package project.smarthome.dataservice.service.mysql.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.mysql.Home;
import project.smarthome.dataservice.repository.mysql.HomeRepository;
import project.smarthome.dataservice.service.mysql.BaseMySQLService;

@Service
public class HomeService extends BaseMySQLService<Home, Long> {

    @Autowired
    private HomeRepository homeRepository;

    @Override
    protected JpaRepository<Home, Long> getRepository() {
        return homeRepository;
    }

    @Override
    protected JpaSpecificationExecutor<Home> getSpecificationExecutor() {
        return homeRepository;
    }
}
