package com.industrial.maintenance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrial.maintenance.entity.*;
import com.industrial.maintenance.repository.MaintenanceRequestRepository;
import java.util.UUID;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InspectionFailureConsumer {
private final MaintenanceRequestRepository repository;
private final MaintenanceEventPublisher events;
private final ObjectMapper mapper;

public InspectionFailureConsumer(MaintenanceRequestRepository repository,MaintenanceEventPublisher events,ObjectMapper mapper){
this.repository=repository;this.events=events;this.mapper=mapper;
}

@RabbitListener(bindings=@QueueBinding(
value=@Queue(value="maintenance.inspection-failures",durable="true"),
exchange=@Exchange(value="iep.domain.events",type="topic",durable="true"),
key={"InspectionFailedEvent"}
))
@Transactional
public void consume(String message){
try{
JsonNode root=mapper.readTree(message);
JsonNode payload=root.path("payload");
UUID equipmentId=UUID.fromString(payload.get("equipmentId").asText());
UUID inspectorId=payload.hasNonNull("inspectorId")?UUID.fromString(payload.get("inspectorId").asText()):new UUID(0,0);
MaintenanceRequest request=new MaintenanceRequest(
equipmentId,
inspectorId,
"Automatic maintenance request — failed inspection",
"An inspection failed and requires corrective action.",
MaintenanceType.CORRECTIVE,
MaintenancePriority.HIGH,
java.time.LocalDate.now()
);
MaintenanceRequest saved=repository.save(request);
events.publish("MaintenanceRequestCreatedEvent",saved);
}catch(Exception ex){
throw new IllegalStateException("Failed inspection could not create maintenance request",ex);
}
}
}
