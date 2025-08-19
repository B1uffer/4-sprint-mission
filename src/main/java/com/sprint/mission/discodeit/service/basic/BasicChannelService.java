package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.basic.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelCantUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) { // 공개채널 생성
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    // 로그 추가
    log.debug("공개채널 생성 완료! 채널 id는 {}", channel.getId());

    channelRepository.save(channel);
    // 로그 추가
    if(channelRepository.existsById(channel.getId())) {
      log.debug("Repository에 channel이 저장되었습니다. ID : {}", channel.getId());
    } else {
      log.warn("Repository에 channel이 저장되지 않았습니다. ID : {}", channel.getId());
    }
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) { // 비밀채널 생성
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    // 로그 추가
    log.info("비밀채널 생성 완료! 채널은 {}", channel);
    channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatuses);
    // 로그 추가
    log.info("readStatusRepository에 readStatuses 저장 완료! {}", readStatuses);

    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) { // 채널 수정
    // 로그 추가
    log.debug("수정할 채널 이름 : {}, 수정할 채널 설명 : {}", request.newName(), request.newDescription());
    String newName = request.newName();
    String newDescription = request.newDescription();

    // 로그 추가
    log.info("채널을 조회합니다. 채널ID : {}", channelId);
    // 로그 추가
    if(!channelRepository.existsById(channelId)) {
      log.warn("채널 ID에 맞는 채널이 존재하지 않습니다. 채널ID : {}", channelId);
    }
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    // 로그 추가
    log.debug("채널 조회 완료 : {}", channelRepository.findById(channelId));
    // 로그 추가
    if(!channel.getType().equals(ChannelType.PUBLIC)) {
      log.warn("비공개 채널은 수정할 수 없습니다. 채널 ID : {}", channelId);
    }
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new PrivateChannelCantUpdateException(ErrorCode.PRIVATE_CHANNEL_CANT_UPDATE);
    }
    // 로그 추가
    log.debug("채널 업데이트 시작, 이름 : {}, 설명 : {}", newName, newDescription);
    channel.update(newName, newDescription);
    // 로그 추가
    log.info("채널 업데이트 성공, 채널이름 : {}, 설명 : {}",channelRepository.getReferenceById(channelId).getName(),
            channelRepository.getReferenceById(channelId).getDescription());
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) { // 채널 삭제
    // 로그 추가
    log.debug("삭제할 채널 조회 시작, 입력된 ID는 {}", channelId);
    if (!channelRepository.existsById(channelId)) {
      // 로그 추가
      log.warn("채널 조회 실패, 입력된 ID는 {}", channelId);
      throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
    }
    // 로그 추가
    log.debug("채널 삭제를 시작합니다. 채널 ID는 {}", channelId);
    messageRepository.deleteAllByChannelId(channelId); // 메세지 삭제
    readStatusRepository.deleteAllByChannelId(channelId); // 읽음상태 삭제
    channelRepository.deleteById(channelId); // 채널삭제
    // 로그 추가
    if(channelRepository.existsById(channelId)) {
      log.warn("채널이 삭제되지 않았습니다. 채널 ID : {}", channelId);
    } else {
      log.info("채널이 삭제되었습니다.");
    }
  }
}
