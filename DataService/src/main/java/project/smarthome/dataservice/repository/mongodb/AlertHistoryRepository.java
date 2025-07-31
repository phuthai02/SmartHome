package project.smarthome.dataservice.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import project.smarthome.common.entity.mongodb.AlertHistory;

public interface AlertHistoryRepository extends MongoRepository<AlertHistory, String> {
}
