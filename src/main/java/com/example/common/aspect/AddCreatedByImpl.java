package com.example.common.aspect;

import com.example.common.entity.MessageEvent;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Log4j2
public class AddCreatedByImpl {

    @Before("@annotation(addCreatedBy)")
    public Object addCreatedBy(JoinPoint joinPoint, AddCreatedBy addCreatedBy) {
        try {
            logger.info("[AddCreatedByImpl][addCreatedBy] Start Adding createdBy");
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            boolean addCreatedAt = addCreatedBy.addCreatedAt();
            Arrays.stream(joinPoint.getArgs())
                    .filter(MessageEvent.class::isInstance)
                    .map(MessageEvent.class::cast)
                    .findFirst()
                    .ifPresent(messageEvent -> {
                        if (StringUtils.isNotBlank(username) && !username.contains("anonymous")) {
                            messageEvent.getPayload().put("createdBy", username);
                        } else {
                            messageEvent.getPayload().put("createdBy", null);
                        }
                        if (addCreatedAt) {
                            messageEvent.getPayload().put("createdAt", System.currentTimeMillis());
                        }
                    });

            return joinPoint;
        } finally {
            logger.info("[AddCreatedByImpl][addCreatedBy] End Adding createdBy");
        }
    }
}
