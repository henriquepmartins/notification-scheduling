package com.henrique.notificationscheduling.infrastructure;

import com.henrique.notificationscheduling.entities.SchedulingModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulingRepository extends JpaRepository<SchedulingModel, Long> {
}
