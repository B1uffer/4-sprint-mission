package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.basic.ErrorCode;
import com.sprint.mission.discodeit.exception.user.AlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
//  private static final Logger logger = LoggerFactory.getLogger(BasicUserService.class);
  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) { // 사용자 생성 로그
      // 로그 추가
      log.debug("create 메서드 호출! 닉네임은 {}, 이메일은 {}", userCreateRequest.username(), userCreateRequest.email());
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
        log.warn("이메일이 이미 존재해요! 이메일은 {}", email); // 로그 추가
      throw new AlreadyExistsException(ErrorCode.DUPLICATE_EMAIL);
      // throw 뒤에 달면 도달할 수 없는 코드가 되어 죽은 코드가 됨
    }
    if (userRepository.existsByUsername(username)) {
        log.warn("사용자 이름이 이미 존재해요! 사용자의 이름은 {}", username); // 로그 추가
      throw new AlreadyExistsException(ErrorCode.DUPLICATE_USER);
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);
    String password = userCreateRequest.password(); // password는 중요정보

    User user = new User(username, email, password, nullableProfile);
    // 로그 추가
    log.info("사용자가 성공적으로 생성! 사용자는 {}", user);
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userRepository.save(user);
    // 로그 추가
    log.info("userRepository에 사용자 저장 완료, 사용자의 ID는 {}",userRepository.findById(user.getId()));
    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) { // 사용자 수정 로그
      // 로그 추가
      log.debug("update 메서드 호출! ID는 {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
        // 로그 추가
        log.warn("새로운 이메일이 이미 존재해요! 그 이메일은 {}", newEmail);
      throw new AlreadyExistsException(ErrorCode.DUPLICATE_EMAIL);
    }
    if (userRepository.existsByUsername(newUsername)) {
        // 로그 추가
        log.warn("새로운 사용자 이름이 이미 존재해요! 이름은 {}", newUsername);
      throw new AlreadyExistsException(ErrorCode.DUPLICATE_USER);
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);

          binaryContentRepository.save(binaryContent);
          // 로그 추가
          log.info("map()을 통해서 binaryContentRepository에 binaryContent 저장 완료!");
          binaryContentStorage.put(binaryContent.getId(), bytes);
          // 로그 추가
          log.info("map()에서 binaryContentStorage에 binaryContent의 id {}를 bytes 형태로 넣었음!", binaryContent.getId());
          return binaryContent;
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfile);
    // 로그 추가
    log.info("새로운 사용자명은 {}, 이메일은 {}",user.getUsername(), user.getEmail());
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) { // 사용자 삭제 로그
    if (userRepository.existsById(userId)) {
        // 로그 추가
        log.warn("존재하지 않는 사용자명 삭제 시도, id는 {}", userId);
      throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
    }

    userRepository.deleteById(userId);
    // 로그 추가
    log.info("사용자 삭제 성공, 삭제된 사용자 ID는 {}",userId);
  }
}
