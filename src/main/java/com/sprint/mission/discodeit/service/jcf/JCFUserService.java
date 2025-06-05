package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.userService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements userService {

    // [유저1, 유저2, 유저3, 유저4, ...]

    private final List<User> userList;

    public JCFUserService() {

        userList = new ArrayList<>(); // ?

    }

    public User createUser(String nickName, String password) {

        User newClient = new User(nickName, password); // User에서 생성자에 public을 안넣어주면 에러발생
        userList.add(newClient);

        return newClient;

    }

    public User readUser(UUID searchId) {
        // 1. userList에서 입력받은 id에 맞는 유저를 찾기
        // 2. return으로 그 유저를 되돌려주기

        User user = null;

        for(int i = 0; i < userList.size(); i++) {

            UUID userId = userList.get(i).getId();

            if(userId.equals(searchId)) {

                user = userList.get(i);

            }

        }

        return user;

    }

    public User updateUser(UUID searchId, String newNickName) {

        // userList에서 입력받은 id에 맞는 유저를 찾은 다음
        // setNickName을 이용하여 nickName을 바꾸기

        User user = null;

        for(int i = 0; i < userList.size(); i++) {

            UUID userId = userList.get(i).getId();

            if(userId.equals(searchId)) {

                user = userList.get(i);
                user.setNickName(newNickName);

            }

        }

        return user;

    }

    public boolean deleteUser(UUID searchId, String password) {

        // userList에서 입력받은 id에 맞는 유저를 찾은 다음
        // 입력한 비밀번호가 가지고 있는 비밀번호와 같다면,
        // userList에서 해당 유저를 지운다

        loop:
        for(int i = 0; i < userList.size(); i++) {

            UUID userId = userList.get(i).getId();

            if(userId.equals(searchId)) {

                if(password.equals(userList.get(i).getPassword())) {

                    userList.remove(i);
                    break loop;

                }

            }

        }

        return true;

    }

}
