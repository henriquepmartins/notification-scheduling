package com.henrique.notificationscheduling.application;

import com.henrique.notificationscheduling.entities.SchedulingModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SchedulingMapper {
    SchedulingModel forModel(SchedulingRequest schedulingRequest);

    SchedulingResponse forResponse(SchedulingModel schedulingModel);

    @Mapping(target = "sentDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(com.henrique.notificationscheduling.shared.Status.CANCELLED)")
    SchedulingModel forCancelModel(SchedulingModel source);
}
