package com.industrial.notification.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrial.notification.entity.Notification;
import com.industrial.notification.repository.NotificationRepository;
import java.util.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.Queue;


@Service
public class NotificationEventConsumer {
private final NotificationRepository repository;
private final ObjectMapper mapper;

public NotificationEventConsumer(NotificationRepository repository,ObjectMapper mapper){
this.repository=repository;
this.mapper=mapper;
}

@RabbitListener(bindings=@QueueBinding(
value=@Queue(value="notification.events",durable="true"),
exchange=@Exchange(value="iep.domain.events",type="topic",durable="true"),
key={"#"}
))
public void consume(String message){
try{
JsonNode root=mapper.readTree(message);
String type=root.path("type").asText("DOMAIN_EVENT");
JsonNode payload=root.path("payload");

UUID userId=null;
if(payload.hasNonNull("assignedTechnicianId")&&!payload.get("assignedTechnicianId").asText().isBlank()){
userId=UUID.fromString(payload.get("assignedTechnicianId").asText());
}else if(payload.hasNonNull("technicianId")&&!payload.get("technicianId").asText().isBlank()){
userId=UUID.fromString(payload.get("technicianId").asText());
}

UUID entityId=null;
if(payload.hasNonNull("equipmentId"))entityId=UUID.fromString(payload.get("equipmentId").asText());
else if(payload.hasNonNull("maintenanceId"))entityId=UUID.fromString(payload.get("maintenanceId").asText());

repository.save(new Notification(userId,title(type),message(type),type,entityId));
}catch(Exception ex){
throw new IllegalStateException("Notification event processing failed",ex);
}
}

private String title(String type){
return switch(type){
case "MaintenanceAssignedEvent"->"Maintenance assignment";
case "MaintenanceCompletedEvent"->"Maintenance completed";
case "InspectionFailedEvent"->"Inspection failed";
case "SparePartLowStockEvent"->"Spare-part inventory alert";
case "FaultReportCreatedEvent"->"New fault report";
case "EquipmentStatusChangedEvent"->"Equipment status changed";
default->"Industrial operations event";
};
}

private String message(String type){
return switch(type){
case "MaintenanceAssignedEvent"->"A maintenance request has been assigned to you.";
case "MaintenanceCompletedEvent"->"A maintenance request has been completed.";
case "InspectionFailedEvent"->"An equipment inspection failed. Corrective maintenance was requested.";
case "SparePartLowStockEvent"->"A spare part has reached its configured minimum inventory.";
case "FaultReportCreatedEvent"->"A new equipment fault has been reported.";
case "EquipmentStatusChangedEvent"->"An equipment status has changed.";
default->"A new operational event has been recorded.";
};
}
}
