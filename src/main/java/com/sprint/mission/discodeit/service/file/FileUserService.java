package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.userService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService {

    // 서비스 인터페이스를 구현하라고 했으니 userService를 구현하는게 맞을것이다
    // JCF 대신 FileIO, 객체 직렬화를 활용해 메서드를 구현하라고 했다
    // 멍청아 공부 시간좀 더 늘려라 으이그

    /*
    public User createUser(String userName, String password) {

        


    }

    public User searchUser(UUID userId) {



    }

    public User updateUser(UUID userID, String newNickName) {



    }

    public User deleteUser(UUID userID) {



    }


     */

    public static <T> void save(Path filePath, T data) { // 파일 저장에 직렬화가 쓰인다. 객체를 byte Stream으로 변경
        // 직렬화
        try(FileOutputStream fos = new FileOutputStream(filePath.toFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {

            oos.writeObject(data);

        } catch(IOException e) {

            throw new RuntimeException(e);

        }

    }

    public static <T> List<T> load(Path directory) {

        // 생성한 파일이 존재한다면
        if(Files.exists(directory)) {

            try {
                // 파일에서 directory와 이름이 같은걸 찾은 다음에
                List<T> list = Files.list(directory)

                        .map(path -> {
                            //역직렬화를 통해 byte stream -> 파일로 변경
                            try (FileInputStream fis = new FileInputStream(path.toFile());
                                 ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {

                                Object data = ois.readObject();

                                return (T)data; // ??

                            }catch(IOException | ClassNotFoundException e) {

                                throw new RuntimeException(e);

                            }

                        })
                        .toList();

                return list;

            } catch(IOException e) {

                throw new RuntimeException(e);

            }
            // end if
        } else {

            return new ArrayList<>();

        }

    }

}
