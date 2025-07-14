package project.smarthome.coreservice.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.User;
import project.smarthome.coreservice.feignclient.user.UserFeignClient;
import project.smarthome.coreservice.model.UserDetailsSecurity;

@Slf4j
@Service
public class UserDetailsSecurityService implements UserDetailsService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userFeignClient.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new UserDetailsSecurity(user);
    }
}
