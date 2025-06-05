package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface messageService {

    // Message CRUD
    // 어느 유저가 어느 채널에서 메세지를 쓴건지 가지고 있어야 됨
    // 생성자에서 유저와 채널을 같이 받고 할당해준다

    Message createMessage(User messageUser, Channel messageChannel, String message);
    // 어느 유저가, 어느 채널에서, 어떤 메세지를 썼는지 알기 위해서 파라미터로 3개를 받는다
    // 그런데 Message의 내가 입력한 생성자의 파라미터에서 User타입의 messageUser를 입력받았다.
    // '어느 유저'가 썼는지 알기 위해선 그 유저의 UUID타입인 id를 알아야만 한다.
    // 그렇다고 메세지를 볼 때 상대방의 uuid가 나오는건 자연스럽지 않다.
    // 따라서 파라미터에 User타입의 messageUser(유저), Channel타입의 messageChannel(채널),
    // String타입의 message(메세지) 총 3개를 받았다.

    List<Message> readMessage(Channel messageChannel, String searchUser);
    // 어느 유저가 어느 채널에서 메세지를 쓴건지 가지고 있어야 됨
    // 메세지 검색하기
    // '그 사람'의 메세지를 검색하기 위해선 중복되는 값은 입력되면 안된다.
    // 유저의 고유한 값인 UUID를 입력받거나 uuid, nickName 둘 다 입력해서 일치하면 메세지를 검색하게끔 해줘야한다
    // 1. nickName을 입력한 다음
    // 2. List<User>에서 파라미터로 입력받은 nickName과 같은 User들만 조회한 다음
    // 3. 그 User들을 출력해주면 디스코드랑 얼추 비슷해진다

    // 1. UUID id를 입력받고
    // 2. List<User>에서 파라미터로 입력받은 id와 같은 User를 뽑아서 출력한다

    // 1. 일단 내가 메세지를 검색하는 채널이 어딘지 알아야됨
    // 2. 그래서 파라미터에 Channel messageChannel을 넣었어요
    // 3. 이 값으로 List<Channel>에서 원하는 채널을 뽑아줄거에요
    // 1. 내가 어떤 사람의 메세지를 검색하기 위해 String searchUser을 넣었어요
    // 2. 이 searchUser로 채널 안에 있는 List<User>중에서 맞는 user을 검색해줄거에요
    // 3. 그리고 그 User의 List<Message>를 전부 출력합니다

    Message updateMessage(Channel messageChannel, User updateUser, Message oldMessage, String newMessage);
    // 어느 유저가 어느 채널에서 메세지를 쓴건지 가지고 있어야 됨
    // 메세지를 수정하기 위해선 어떤 채널의 어떤 유저의 어떤 메세지인지 정확히 알아야된다
    // 그리고 새로운 메세지를 기존 메세지에 덮어씌워준다
    // 그래서 나는 파라미터를 총 4개 넣었다.


    Message deleteMessage(Channel messageChannel, Message deleteMessage, UUID id);
    // 어느 유저가 어느 채널에서 메세지를 쓴건지 가지고 있어야 됨
    // 디스코드에서 메세지를 삭제할땐 어떤 채널에서 / 많은 메세지 중 어떤 메세지를 / 삭제할 것인지 정합니다
    // 따라서 Channel messageChannel, Message deleteMessage 를 받습니다.
    // 또한 삭제하는 메세지가 내 메세지인지 확인해야 하기 때문에 '나'를 정확하게 알기 위해서 uid를 받아와야 합니다.

}
