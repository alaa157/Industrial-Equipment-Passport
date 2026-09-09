package com.industrial.equipment.service;

import com.industrial.equipment.dto.FaultRequest;
import com.industrial.equipment.entity.*;
import com.industrial.equipment.repository.FaultReportRepository;
import java.util.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FaultService {
private final FaultReportRepository faults;
private final EquipmentService equipment;
private final EventPublisher events;

public FaultService(FaultReportRepository faults,EquipmentService equipment,EventPublisher events){
this.faults=faults;this.equipment=equipment;this.events=events;
}

@Transactional
public FaultReport create(UUID equipmentId,FaultRequest request){
UUID reporter=UUID.fromString(((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getToken().getSubject());
FaultReport fault=faults.save(new FaultReport(equipment.get(equipmentId),reporter,request.title(),request.description(),request.severity()));
events.publish("FaultReportCreatedEvent",Map.of("faultId",fault.getId(),"equipmentId",equipmentId,"severity",fault.getSeverity().name(),"title",fault.getTitle()));
return fault;
}

@Transactional(readOnly=true)
public List<FaultReport> findByEquipment(UUID equipmentId){return faults.findByEquipmentIdOrderByReportedAtDesc(equipmentId);}
}
