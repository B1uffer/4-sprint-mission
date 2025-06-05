package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {

    // 메세지는 생성 후 손쉽게 수정, 삭제, 검색이 이루어질 수 있어야한다
    // 메세지는 중복이 가능하니까 Set이 아닌 List로 사용해야겠다
    // Channel에서 메세지 클래스를 사용하려고 보니 메세지 클래스에서 List를 사용하지 않아도 될 것 같다
    /*
     * 메세지
     * (채널) 유저 : [입력날짜, 메세지]
     *
     */
    // 식별자 있어야됨
    // 누가 어느 채널에서 보내는지 나옴
    // 생성자에서 유저와 채널을 같이 받고 할당해준다

    private User messageUser;
    private Channel messageChannel;
    private String message;
    private Long createdAt;
    private Long updatedAt;

    // 생성자
    // 메세지를 입력받으면 메세지를 저장하고, 입력된 날짜를 생성한다

    // 어느 유저가 어느 채널에서 메세지를 쓴건지 가지고 있어야 됨
    public Message(User messageUser, Channel messageChannel, String message) { // public으로 만들어야한다

        this.messageUser = messageUser;
        this.messageChannel = messageChannel;
        this.message = message; // 안적어도 될 것 같다
        createdAt = System.currentTimeMillis();

    }

    // getter 함수 정의

    public String getMessage() {

        return message;

    }

    public Long getCreatedAt() {

        return createdAt;

    }

    public Long getUpdatedAt() {

        return updatedAt;

    }

    // update함수 정의 setter함수로 바꾸기
    // 새로운 메세지를 받으면 그 메세지를 그대로 받는다
    public void setMessage(String newMessage) {

        message = newMessage;
        updatedAt = System.currentTimeMillis();

    }









    /*

    // 메세지를 입력할 수 있어야한다?
    // 메세지는 수정할 수 있어야 하고, 지울 수도 있어야 하고,(update, delete)
    // 메세지를 입력도 할 수 있어야 하고(create), 누군가가 내 게시물을 읽을 수 있어야한다, 조회한다고 함
    // 내가 쓴 메세지를 검색도 할 수 있어야 한다(read)
    // 메세지는 '기본적으로' 메세지를 기입하는 공간, 그 메세지를 채팅창으로 보내는 확인버튼이 있다

    private static UUID id;
    private String messageBox; // 메세지 공간
    private boolean commit = true; // 확인버튼?

    // 메세지박스는 String으로 이루어진 배열이거나 String이어야한다

    // 생성자
    Message(UUID id, String messageBox) {

        this.id = id;
        this.messageBox = messageBox;


    }

    public void setMessageBox(String messageBox) {

        this.messageBox = messageBox;

    }

    public String getMessageBox() {

        return messageBox;

    }

    public boolean getCommit() {

        return commit;

    }


    public String toString() {

        return "id : " + id + ", message : " + messageBox + ", commit : " + commit;

    }

    // 한명의 회원이 메세지를 가지고 있다면
    // 그 메세지도 회원을 가지고 있어야한다

     */


}
