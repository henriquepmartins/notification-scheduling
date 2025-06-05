package com.henrique.notificationscheduling.application;

import com.henrique.notificationscheduling.infrastructure.SchedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class SchedulingService {
    private final SchedulingRepository schedulingRepository;
    private final SchedulingMapper schedulingMapper;

    public SchedulingResponse saveScheduling(SchedulingRequest schedulingRequest) {
        return schedulingMapper.forResponse(schedulingRepository.save(schedulingMapper.forModel(schedulingRequest)));
    }
}
