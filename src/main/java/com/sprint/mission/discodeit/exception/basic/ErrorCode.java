package com.sprint.mission.discodeit.exception.basic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode { // ErrorCode Enum 클래스를 통해 예외 코드명과 메시지 정의
    USER_NOT_FOUND, // 사용자 id를 찾을 수 없을때
    USER_CREATED, // 사용자 생성 완료
    DUPLICATE_USER, // 사용자 id가 중복될때
    DUPLICATE_EMAIL, // 사용자 email이 중복될 때
    USER_UPDATE, // 사용자 업데이트 완료
    USER_DELETED, // 사용자 삭제 완료

    CHANNEL_NOT_FOUND, // 채널 id를 찾을 수 없을 때
    PUBLIC_CHANNEL_CREATED, // 공개채널 생성완료
    PUBLIC_CHANNEL_UPDATE, // 공개채널 수정완료
    PRIVATE_CHANNEL_CREATED, // 비공개채널 생성완료
    PRIVATE_CHANNEL_CANT_UPDATE, // 비공개채널 수정완료
    CHANNEL_DELETED, // 채널 삭제 완료

    MESSAGE_NOT_FOUND, // 메세지 id를 찾을 수 없을 때
    MESSAGE_CREATED, // 메세지 생성 완료
    MESSAGE_UPDATED, // 메세지 수정 완료
    MESSAGE_DELETED, // 메세지 삭제 완료

    BINARY_CONTENT_NOT_FOUND, // 파일을 찾을 수 없음
    BINARY_CONTENT_UPLOAD, // 파일 업로드 완료
    BINARY_CONTENT_DOWNLOAD;// 파일 다운로드 완료

    private int status;
    private String message;
}
