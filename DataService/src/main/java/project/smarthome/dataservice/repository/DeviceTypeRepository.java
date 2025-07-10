package project.smarthome.dataservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.smarthome.common.entity.DeviceType;

public interface DeviceTypeRepository extends JpaRepository<DeviceType, Long> {
}
