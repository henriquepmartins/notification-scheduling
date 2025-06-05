package com.henrique.notificationscheduling.infrastructure;

import com.henrique.notificationscheduling.application.SchedulingRequest;
import com.henrique.notificationscheduling.application.SchedulingResponse;
import com.henrique.notificationscheduling.application.SchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agendamento")
public class SchedulingController {
    private final SchedulingService schedulingService;

    @PostMapping
    public ResponseEntity<SchedulingResponse> saveScheduling(@RequestBody SchedulingRequest schedulingRequest) {
        return ResponseEntity.ok(schedulingService.saveScheduling(schedulingRequest));
    }
}
