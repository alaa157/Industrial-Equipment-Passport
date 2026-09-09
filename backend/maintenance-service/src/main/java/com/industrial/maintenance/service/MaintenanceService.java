package com.industrial.maintenance.service;

import com.industrial.maintenance.dto.*;
import com.industrial.maintenance.entity.*;
import com.industrial.maintenance.repository.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaintenanceService {
private final MaintenanceRequestRepository repository;
private final MaintenanceScheduleRepository schedules;
private final MaintenanceEventPublisher events;

public MaintenanceService(MaintenanceRequestRepository repository,MaintenanceScheduleRepository schedules,MaintenanceEventPublisher events){
this.repository=repository;this.schedules=schedules;this.events=events;
}

private UUID currentUser(){
return UUID.fromString(((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getToken().getSubject());
}

@Transactional
public MaintenanceResponse create(MaintenanceRequestDto dto){
MaintenanceRequest request=new MaintenanceRequest(dto.equipmentId(),currentUser(),dto.title(),dto.description(),dto.type(),dto.priority(),dto.dueDate());
MaintenanceRequest saved=repository.save(request);
events.publish("MaintenanceRequestCreatedEvent",saved);
return MaintenanceResponse.from(saved);
}

@Transactional(readOnly=true)
public List<MaintenanceResponse> list(MaintenanceStatus status,UUID technician){
List<MaintenanceRequest> data;
if(technician!=null)data=repository.findByAssignedTechnicianIdOrderByCreatedAtDesc(technician);
else if(status!=null)data=repository.findByStatusOrderByCreatedAtDesc(status);
else data=repository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC,"createdAt"));
return data.stream().map(MaintenanceResponse::from).toList();
}

@Transactional(readOnly=true)
public MaintenanceRequest get(UUID id){return repository.findById(id).orElseThrow(()->new NoSuchElementException("Maintenance request not found"));}

@Transactional
public MaintenanceResponse assign(UUID id,UUID technician){
MaintenanceRequest request=get(id);
if(EnumSet.of(MaintenanceStatus.COMPLETED,MaintenanceStatus.CANCELLED).contains(request.getStatus()))throw new IllegalStateException("Completed or cancelled maintenance cannot be reassigned");
request.assign(technician);
MaintenanceRequest saved=repository.save(request);
events.publish("MaintenanceAssignedEvent",saved);
return MaintenanceResponse.from(saved);
}

@Transactional
public MaintenanceResponse start(UUID id){
MaintenanceRequest request=get(id);
if(request.getStatus()!=MaintenanceStatus.ASSIGNED&&request.getStatus()!=MaintenanceStatus.ON_HOLD)throw new IllegalStateException("Maintenance must be assigned or on hold before starting");
request.start();
MaintenanceRequest saved=repository.save(request);
events.publish("MaintenanceStartedEvent",saved);
return MaintenanceResponse.from(saved);
}

@Transactional
public MaintenanceResponse hold(UUID id){
MaintenanceRequest request=get(id);
if(request.getStatus()!=MaintenanceStatus.IN_PROGRESS)throw new IllegalStateException("Only active maintenance can be put on hold");
request.hold();
MaintenanceRequest saved=repository.save(request);
events.publish("MaintenanceOnHoldEvent",saved);
return MaintenanceResponse.from(saved);
}

@Transactional
public MaintenanceResponse complete(UUID id,CompleteMaintenanceRequest dto){
MaintenanceRequest request=get(id);
if(request.getStatus()!=MaintenanceStatus.IN_PROGRESS)throw new IllegalStateException("Only in-progress maintenance can be completed");
request.complete(dto.actualDurationMinutes(),dto.maintenanceCost(),dto.laborNotes(),dto.completionNotes());
MaintenanceRequest saved=repository.save(request);
events.publish("MaintenanceCompletedEvent",saved);
return MaintenanceResponse.from(saved);
}

@Transactional
public MaintenanceResponse cancel(UUID id){
MaintenanceRequest request=get(id);
if(request.getStatus()==MaintenanceStatus.COMPLETED)throw new IllegalStateException("Completed maintenance cannot be cancelled");
request.cancel();
MaintenanceRequest saved=repository.save(request);
events.publish("MaintenanceCancelledEvent",saved);
return MaintenanceResponse.from(saved);
}

@Transactional(readOnly=true)
public Map<String,Object> dashboard(){
return Map.of(
"open",repository.countByStatus(MaintenanceStatus.OPEN),
"assigned",repository.countByStatus(MaintenanceStatus.ASSIGNED),
"inProgress",repository.countByStatus(MaintenanceStatus.IN_PROGRESS),
"completed",repository.countByStatus(MaintenanceStatus.COMPLETED),
"critical",repository.countByPriority(MaintenancePriority.CRITICAL),
"overdue",repository.countByDueDateBeforeAndStatusNot(LocalDate.now(),MaintenanceStatus.COMPLETED)
);
}
}
