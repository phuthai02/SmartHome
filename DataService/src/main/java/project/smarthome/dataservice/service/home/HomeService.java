package project.smarthome.dataservice.service.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.Home;
import project.smarthome.dataservice.repository.HomeRepository;
import project.smarthome.dataservice.service.BaseService;

@Service
public class HomeService extends BaseService<Home, Long> {
    @Autowired
    private HomeRepository homeRepository;

    @Override
    protected JpaRepository<Home, Long> getRepository() {
        return homeRepository;
    }
}
