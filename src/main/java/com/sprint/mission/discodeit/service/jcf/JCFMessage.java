package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.messageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessage implements messageService {

    private final List<Message> messageList;

    JCFMessage() {

        messageList = new ArrayList<>();

    }


    public Message createMessage(User messageUser, Channel messageChannel, String message) {

        // 새로운 메세지를 생성한다
       Message newMessage = new Message(messageUser, messageChannel, message);
       messageList.add(newMessage);


    }

    public List<Message> readMessage(Channel messageChannel, String searchUser) {



    }

    public Message updateMessage(Channel messageChannel, User updateUser, Message oldMessage, String newMessage) {



    }

    public Message deleteMessage(Channel messageChannel, Message deleteMessage, UUID id) {



    }


}
