package com.henrique.notificationscheduling.infrastructure;

import com.henrique.notificationscheduling.application.SchedulingRequest;
import com.henrique.notificationscheduling.application.SchedulingResponse;
import com.henrique.notificationscheduling.application.SchedulingService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/agendamento")
public class SchedulingController {
    private final SchedulingService schedulingService;

    @PostMapping
    public ResponseEntity<SchedulingResponse> saveScheduling(@RequestBody SchedulingRequest schedulingRequest) {
        return ResponseEntity.ok(schedulingService.saveScheduling(schedulingRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SchedulingResponse>> getAllScheduling() {
        return ResponseEntity.ok(schedulingService.getAllScheduling());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SchedulingResponse> getSchedulingById(@PathVariable Long id) {
        return ResponseEntity.ok(schedulingService.getSchedulingById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelScheduling(@PathVariable Long id) {
        schedulingService.cancelScheduling(id);
        return ResponseEntity.accepted().build();
    }
}
