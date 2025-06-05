package com.henrique.notificationscheduling.application;

import com.henrique.notificationscheduling.infrastructure.SchedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class SchedulingService {
    private final SchedulingRepository schedulingRepository;
}
