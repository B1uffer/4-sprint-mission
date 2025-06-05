package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    
    // 유저들이 채널로 입장해야한다
    // 채널에는 기본적으로 이름, 정원 수를 가지고 있어야 생성할 수 있다. 이건 리스트로 만들어도 되지 않을까?
    // 채널이라는 배열 안에 유저들이 들어가야하고 채팅을 입력할 수 있어야함
    // 채널을 생성할 수 있어야 하고, 채널을 수정도 할 수 있어야 하고,
    // 채널 코드를 만들어서 조회?도 하나? 채널 내부를 조회? 할 수도 있어야하고, 채널을 지울 수도 있어야 한다.
    // 채널에는 메뉴도 있다. 메뉴도 리스트 형태가 맞을까?

    // channel에서 유저를 들고 있는걸, 꼭 list로 들어야할까?
    // 채널이 멤버리스트를 들고 있다면,
    // 멤버도 채널리스트를 들고 있어야 한다
    // 서로가 서로를 들 수 있도록 구성해야함

    private UUID id;
    private String channelName; // 채널의 이름
    private List<String> menu; // 메뉴
    // 메뉴 안에서도 메세지를 입력할 수 있어야한다

    Channel() {}

    Channel(String channelName) {

        this.channelName = channelName;

    }

    public String getChannelName() {

        return channelName;

    }

    public String toString() {

        return "UUID : " + id + ", channelName : " + channelName;

    }

    /* intelliJ가 자동완성 해준 것들
    private int id;
    private String name;
    private String description;
    private String url;
    private String image;
    private String type;
    private String status;
    */

}
