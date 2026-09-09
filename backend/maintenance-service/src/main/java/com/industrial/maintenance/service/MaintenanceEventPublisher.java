package com.industrial.maintenance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrial.maintenance.entity.MaintenanceRequest;
import java.time.Instant;
import java.util.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceEventPublisher {
private final RabbitTemplate rabbit;
private final ObjectMapper mapper;

public MaintenanceEventPublisher(RabbitTemplate rabbit,ObjectMapper mapper){
this.rabbit=rabbit;this.mapper=mapper;
}

public void publish(String type,MaintenanceRequest maintenance){
try{
Map<String,Object> payload=new LinkedHashMap<>();
payload.put("maintenanceId",maintenance.getId());
payload.put("equipmentId",maintenance.getEquipmentId());
payload.put("requesterId",maintenance.getRequesterId());
payload.put("assignedTechnicianId",maintenance.getAssignedTechnicianId()==null?"":maintenance.getAssignedTechnicianId());
payload.put("title",maintenance.getTitle());
payload.put("status",maintenance.getStatus().name());
payload.put("priority",maintenance.getPriority().name());

Map<String,Object> event=new LinkedHashMap<>();
event.put("type",type);
event.put("timestamp",Instant.now().toString());
event.put("payload",payload);

rabbit.convertAndSend("iep.domain.events",type,mapper.writeValueAsString(event));
}catch(Exception ex){
throw new IllegalStateException("Unable to publish maintenance event",ex);
}
}
}
