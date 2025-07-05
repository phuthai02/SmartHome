package project.smarthome.dataservice.repository;

import org.springframework.data.repository.CrudRepository;
import project.smarthome.common.entity.Home;

public interface HomeRepository extends CrudRepository<Home, Long> {
}
