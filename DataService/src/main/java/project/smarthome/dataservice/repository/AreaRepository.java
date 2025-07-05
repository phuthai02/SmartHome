package project.smarthome.dataservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.smarthome.common.entity.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
}
