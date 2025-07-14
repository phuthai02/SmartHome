package project.smarthome.dataservice.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.User;
import project.smarthome.dataservice.repository.UserRepository;
import project.smarthome.dataservice.service.BaseService;

import java.util.Optional;

@Service
public class UserService extends BaseService<User, Long> {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
