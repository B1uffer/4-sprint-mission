package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface userService {

    // User CRUD(create, read, update, delete)
    User createUser(String nickName, String password);
    // 유저 생성하기
    // nickName과 password를 입력하면 새로운 계정이 생성된다

    User readUser(UUID id);
    // 유저 검색하기
    // UUID 타입의 id로 입력하면 개인을 찾을 수 있지만 사용자 입장에서 번거로울 수 있다.
    // nickName은 입력하기 편리하지만 중복될 수 있기 때문에 정확하게 찾기 힘들다.
    // 단 한명의 유저를 찾는 것부터 시작하려고 id를 선택했다.

    User updateUser(UUID id, String newNickName);
    // 닉네임만 바꿔보기
    // 로그인을 했다는 전제하에 새로운 닉네임을 입력한다
    // 만약, 기존 닉네임과 바꾸려는 닉네임이 같다면 닉네임을 바꾸지 못하게 한다
    // 그 이외의 경우는 기존의 닉네임을 지우고 입력받은 닉네임을 적용시킨다
    // 수정한 날짜는 현재시간으로 설정해줌

    boolean deleteUser(UUID id, String password); // 여기에서 id는 유저를 찾는 역할을 한다
    // 유저 지우기
    // 비밀번호를 입력받고
    // 유저의 비밀번호와 입력받은 비밀번호가 같다면,
    // 유저를 지운다
    // 그 외에는 지울 수 없게끔 한다.

}
