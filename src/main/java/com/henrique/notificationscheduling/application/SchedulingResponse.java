package com.henrique.notificationscheduling.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.henrique.notificationscheduling.shared.Status;

import java.time.LocalDateTime;

public record SchedulingResponse(Long id, String recipientMail, String recipientPhone, String message,
                                 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM_yyyy HH:mm:ss")
                                 LocalDateTime sentDate, Status status) {

}
