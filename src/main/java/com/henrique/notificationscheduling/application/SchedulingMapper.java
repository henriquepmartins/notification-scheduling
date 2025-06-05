package com.henrique.notificationscheduling.application;

import com.henrique.notificationscheduling.entities.SchedulingModel;
import org.mapstruct.Mapper;

import static org.mapstruct.ap.internal.gem.MappingConstantsGem.ComponentModelGem.SPRING;

@Mapper(componentModel = SPRING)
public interface SchedulingMapper {
    SchedulingModel forModel(SchedulingRequest schedulingRequest);

    SchedulingResponse forResponse(SchedulingModel schedulingModel);
}
