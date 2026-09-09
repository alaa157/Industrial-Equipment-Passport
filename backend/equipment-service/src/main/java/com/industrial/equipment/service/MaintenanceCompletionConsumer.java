package com.industrial.equipment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrial.equipment.entity.EquipmentStatus;
import com.industrial.equipment.repository.EquipmentRepository;
import java.util.UUID;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaintenanceCompletionConsumer {
private final EquipmentRepository equipment;
private final ObjectMapper mapper;

public MaintenanceCompletionConsumer(EquipmentRepository equipment,ObjectMapper mapper){
this.equipment=equipment;
this.mapper=mapper;
}

@RabbitListener(bindings=@QueueBinding(
value=@Queue(value="equipment.maintenance-events",durable="true"),
exchange=@Exchange(value="iep.domain.events",type="topic",durable="true"),
key={"MaintenanceCompletedEvent"}
))
@Transactional
public void completed(String message){
try{
JsonNode root=mapper.readTree(message);
JsonNode payload=root.path("payload");
UUID equipmentId=UUID.fromString(payload.get("equipmentId").asText());
equipment.findById(equipmentId).ifPresent(asset->{
if(asset.getStatus()==EquipmentStatus.UNDER_MAINTENANCE){
asset.setStatus(EquipmentStatus.ACTIVE);
equipment.save(asset);
}
});
}catch(Exception ex){
throw new IllegalStateException("Maintenance completion event failed",ex);
}
}
}
