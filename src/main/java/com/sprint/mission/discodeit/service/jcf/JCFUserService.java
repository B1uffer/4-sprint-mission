package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.userService;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;


public class JCFUserService implements userService {

    // [유저1, 유저2, 유저3, 유저4, ...]

    private final Map<UUID,User> userList;

    public JCFUserService() {

        userList = new HashMap<>();

    }

    public User createUser(String nickName, String password) {

        UUID userId = UUID.randomUUID();
        User newClient = new User(nickName, password); // User에서 생성자에 public을 안넣어주면 에러발생
        userList.put(userId,newClient);

        return newClient;

    }

    public User searchUser(UUID searchId) {

        if (userList.containsKey(searchId)) {
            User findUser = userList.get(searchId);
            return findUser;
        }

        return null;
    }

    public User updateUser(UUID userId, String newNickName) {
        User update = userList.get(userId);

        if(newNickName != null && !newNickName.equals(update.getNickName())) {
            update.updateNickName(newNickName);
        }

        return null;
    }

    public User deleteUser(UUID searchId) {

        if(!userList.containsKey(searchId)) {

            throw new NoSuchElementException("너는 계정을 삭제 할 수 없어!");

        }

        return userList.remove(searchId);

    }

}
