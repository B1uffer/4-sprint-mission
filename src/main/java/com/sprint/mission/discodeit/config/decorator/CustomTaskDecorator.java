package com.sprint.mission.discodeit.config.decorator;

import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class CustomTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        /**
         * 현재 요청의 RequestAttributes를 가져온다
         * RequestAttributes와 RequestAttribute 주의
         * https://yeo-computerclass.tistory.com/304
         */
        RequestAttributes attribute = RequestContextHolder.currentRequestAttributes();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(attribute);
            } finally {
                RequestContextHolder.currentRequestAttributes();
            }
        };
    }
}
