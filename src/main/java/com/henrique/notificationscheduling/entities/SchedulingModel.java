package com.henrique.notificationscheduling.entities;

import com.henrique.notificationscheduling.shared.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "scheduling")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SchedulingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipientMail;
    private String recipientName;
    private String recipientPhone;
    private LocalDateTime sentDate;
    private LocalDateTime scheduleDate;
    private LocalDateTime modifiedDate;
    private String message;
    private Status status;

    @PrePersist
    private void prePersist() {
        scheduleDate = LocalDateTime.now();
        status = Status.SCHEDULED;
    }
}
