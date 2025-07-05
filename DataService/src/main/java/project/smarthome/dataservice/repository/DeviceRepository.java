package project.smarthome.dataservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.smarthome.common.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
