package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

public interface channelService {

    // channel에 대한 CRUD

    Channel createChannel(String channelName);
    // 채널을 만들 때 디스코드에서는 채널명만 입력하면 생성됩니다.
    // 파라미터에는 아직 List<Channel>에 없는 새로운 String형을 입력받습니다.

    Channel readChannel(Channel channelName);
    // 채널 검색하기
    // 디스코드에서 채널을 검색할 때 채널명만 검색하면 채널명이 포함된 채널들이 검색됩니다.

    Channel updateChannel(Channel channelName);
    // 채널 수정하기
    // 채널을 수정할 땐 채널을 선택한 뒤 채널이름, 채널주제 등을 변경할 수 있습니다.
    // 이를 변경하기 위해선 내가 바꾸고 싶은 채널을 선택한 다음 채널이름 등을 바꿔야합니다.
    // 이를 식별하기 위해서 파라미터로 Channel 타입의 channelName을 입력받습니다.

    Channel deleteChannel(Channel channelName);
    // 채널 삭제하기
    // 채널을 삭제할 때 채널 안에 모든 메세지와 유저목록이 함께 삭제되어야 합니다.
    // 파라미터로 Channel 이름을 받아온 다음에
    // 해당 채널을 삭제합니다.
}
