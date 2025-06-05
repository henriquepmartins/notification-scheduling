package com.henrique.notificationscheduling.application;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDateTime;

public record SchedulingResponse(Long id, String recipientMail, String recipientPhone, String message,
                                 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                                 LocalDateTime sentDate) {

}
