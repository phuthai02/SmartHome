package project.smarthome.dataservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.smarthome.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
