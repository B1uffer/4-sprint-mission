package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscodeitUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("유저 이름을 찾을 수 없음"));

        log.info("user 찾음, {}", user);

        BinaryContentDto binaryContentDto = new BinaryContentDto(
                user.getProfile().getId(),
                user.getProfile().getFileName(),
                user.getProfile().getSize(),
                user.getProfile().getContentType()
        );
        UserDto userDto = new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentDto,
                user.getStatus().isOnline());

        DiscodeitUserDetails userDetails = new DiscodeitUserDetails(userDto, user.getPassword());

//        return org.springframework.security.core.userdetails.User.builder() // UserDetails 객체 생성
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .roles("USER")
//                .build();
        return userDetails;
    }
}
