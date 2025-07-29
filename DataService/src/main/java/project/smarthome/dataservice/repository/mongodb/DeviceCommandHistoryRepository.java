package project.smarthome.dataservice.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import project.smarthome.common.entity.mongodb.DeviceCommandHistory;

public interface DeviceCommandHistoryRepository extends MongoRepository<DeviceCommandHistory, String> {
}
