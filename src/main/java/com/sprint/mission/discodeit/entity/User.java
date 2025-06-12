package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id; // 객체를 식별하기 위한 id, 변경할 수 없다
    private String nickName; // 닉네임, 변경 가능하다
    private String password; // 비밀번호, 변경 가능하다
    private final Long createdAt; // 객체의 생성시간을 유닉스 타임스탬프로 나타내기 위한 필드,변경할 수 없다
    private Long updatedAt; // 객체의 수정시간을 유닉스 타임스탬프로 나타내기 위한 필드, 변경할 수 있다
    
    /*
    
        UUID(Universally Unique Identifier)
        범용 고유 식별자이며 중복이 되지 않는 유일한 값을 구성하고자 할 때 주로 사용됨
        세션 식별자, 쿠키값, 무작위 DB등에 사용된다
        128비트이며 길이는 36자리, 4개의 하이픈(-)과 32개의 16진수 문자열로 되어있음
        8자리-4자리-4자리-4자리-12자리
        UUID.randomUUID();
    
     */


    // 생성자

    public User(String nickName, String password) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.nickName = nickName;
        this.password = password;
    }


    // 각 필드를 반환하는 getter함수 정의
    public UUID getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return password;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    // 필드를 수정하는 update 함수 정의, 닉네임과 비밀번호만 변경한다
    // 얘들 boolean이 되면 안된다 수정하자

    public void updateNickName(String newNickName) {
            nickName = newNickName;
            updatedAt = System.currentTimeMillis();
    }


    public void updatePassword(String newPassword) {
            password = newPassword;
            updatedAt = System.currentTimeMillis();
    }

    public String toCSV() {

        return id + "," + nickName + "," + password + "," + createdAt + "," + updatedAt;

    }

}
