package project.smarthome.dataservice.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.smarthome.common.entity.mysql.Home;

public interface HomeRepository extends JpaRepository<Home, Long>, JpaSpecificationExecutor<Home> {
}
