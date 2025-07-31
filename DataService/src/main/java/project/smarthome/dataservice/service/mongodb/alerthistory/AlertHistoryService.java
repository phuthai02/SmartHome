package project.smarthome.dataservice.service.mongodb.alerthistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.mongodb.AlertHistory;
import project.smarthome.dataservice.repository.mongodb.AlertHistoryRepository;
import project.smarthome.dataservice.service.mongodb.BaseMongoDBService;

@Service
public class AlertHistoryService extends BaseMongoDBService<AlertHistory, String> {

    @Autowired
    private AlertHistoryRepository alertHistoryRepository;

    @Override
    protected MongoRepository<AlertHistory, String> getRepository() {
        return alertHistoryRepository;
    }

    @Override
    protected Class<AlertHistory> getEntityClass() {
        return AlertHistory.class;
    }
}
