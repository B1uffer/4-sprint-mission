package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails {
    private final UserDto userDto;
    private final String password;

    @Override
    public boolean isAccountNonExpired() { // 어카운트가 만료되지 않았나요? 네
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 어카운트가 잠기지 않았나요?? 네
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 크리덴셜이 만료되지 않았나요? 네
        return true;
    }

    @Override
    public boolean isEnabled() { // 사용 가능합니가??? 네
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return userDto.username();
    }
}
