package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface userService {

   public User createUser(String userName, String password);

   public User searchUser(UUID userId);

   public User updateUser(UUID userID, String newNickName);

   public User deleteUser(UUID userID);

}
