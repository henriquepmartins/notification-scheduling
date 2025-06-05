package com.henrique.notificationscheduling.application;

import com.henrique.notificationscheduling.entities.SchedulingModel;
import com.henrique.notificationscheduling.infrastructure.SchedulingRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.SchedulingException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class SchedulingService {
    private final SchedulingRepository schedulingRepository;
    private final SchedulingMapper schedulingMapper;

    public SchedulingResponse saveScheduling(SchedulingRequest schedulingRequest) {
        return schedulingMapper.forResponse(schedulingRepository.save(schedulingMapper.forModel(schedulingRequest)));
    }

    public SchedulingResponse getSchedulingById(Long id) {
        return schedulingMapper.forResponse(schedulingRepository.findById(id).orElseThrow(() -> new SchedulingException("ERROR: Id not found")));
    }

    public List<SchedulingResponse> getAllScheduling() {
        return schedulingRepository.findAll().stream()
                .map(schedulingMapper::forResponse)
                .collect(Collectors.toList());
    }

    public SchedulingResponse cancelScheduling(Long id) {
        SchedulingModel schedulingModel = schedulingRepository.findById(id)
                .orElseThrow(() -> new SchedulingException("ERROR: Id not found"));
        return schedulingMapper.forResponse(schedulingMapper.forCancelModel(schedulingModel));
    }
}
