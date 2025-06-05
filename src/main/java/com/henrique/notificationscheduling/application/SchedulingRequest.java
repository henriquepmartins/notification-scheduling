package com.henrique.notificationscheduling.application;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SchedulingRequest(String recipientMail, String recipientPhone, String message,
                                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM_yyyy HH:mm:ss")
                                LocalDateTime sentDate) {

}