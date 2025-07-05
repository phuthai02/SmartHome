package project.smarthome.dataservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.smarthome.common.entity.Home;

public interface HomeRepository extends JpaRepository<Home, Long> {
}
