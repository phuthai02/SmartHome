package project.smarthome.dataservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.smarthome.model.entity.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
}
