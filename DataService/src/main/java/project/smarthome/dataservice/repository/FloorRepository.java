package project.smarthome.dataservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.smarthome.model.entity.Floor;

public interface FloorRepository extends JpaRepository<Floor, Long> {
}
