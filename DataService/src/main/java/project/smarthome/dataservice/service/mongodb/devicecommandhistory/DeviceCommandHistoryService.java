package project.smarthome.dataservice.service.mongodb.devicecommandhistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.mongodb.DeviceCommandHistory;
import project.smarthome.dataservice.repository.mongodb.DeviceCommandHistoryRepository;
import project.smarthome.dataservice.service.mongodb.BaseMongoDBService;

@Service
public class DeviceCommandHistoryService extends BaseMongoDBService<DeviceCommandHistory, String> {

    @Autowired
    private DeviceCommandHistoryRepository deviceCommandHistoryRepository;

    @Override
    protected MongoRepository<DeviceCommandHistory, String> getRepository() {
        return deviceCommandHistoryRepository;
    }

    @Override
    protected Class<DeviceCommandHistory> getEntityClass() {
        return DeviceCommandHistory.class;
    }
}
