package project.smarthome.dataservice.service.mysql.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.mysql.User;
import project.smarthome.dataservice.repository.mysql.UserRepository;
import project.smarthome.dataservice.service.mysql.BaseMySQLService;

import java.util.Optional;

@Service
public class UserService extends BaseMySQLService<User, Long> {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    protected JpaSpecificationExecutor<User> getSpecificationExecutor() {
        return userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
