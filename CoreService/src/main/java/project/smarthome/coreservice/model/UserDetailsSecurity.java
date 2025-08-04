package project.smarthome.coreservice.model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.smarthome.common.dto.response.UserResponse;
import project.smarthome.common.entity.mysql.User;
import project.smarthome.common.utils.Constants;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UserDetailsSecurity implements UserDetails {

    private final User user;

    public UserResponse getUserInfo() {
        return user.getUserInfo();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Constants.Status.LOCKED.equalsIgnoreCase(user.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Constants.Status.ACTIVE.equalsIgnoreCase(user.getStatus());
    }
}
