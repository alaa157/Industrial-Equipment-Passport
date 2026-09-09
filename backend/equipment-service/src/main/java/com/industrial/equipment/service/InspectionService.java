package com.industrial.equipment.service;

import com.industrial.equipment.dto.InspectionRequest;
import com.industrial.equipment.entity.*;
import com.industrial.equipment.repository.InspectionRepository;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InspectionService {
private final InspectionRepository inspections;
private final EquipmentService equipment;
private final EventPublisher events;

public InspectionService(InspectionRepository inspections,EquipmentService equipment,EventPublisher events){
this.inspections=inspections;this.equipment=equipment;this.events=events;
}

@Transactional
public Inspection create(UUID equipmentId,UUID inspectorId,InspectionRequest request){
Equipment e=equipment.get(equipmentId);
Inspection inspection=inspections.save(new Inspection(e,inspectorId,request.inspectionDate(),request.checklist(),request.result(),request.notes(),request.nextInspectionDate()));
if(request.result()==InspectionResult.FAILED){
events.publish("InspectionFailedEvent",Map.of("inspectionId",inspection.getId(),"equipmentId",equipmentId,"inspectorId",inspectorId));
}
return inspection;
}

@Transactional(readOnly=true)
public List<Inspection> findByEquipment(UUID equipmentId){return inspections.findByEquipmentIdOrderByInspectionDateDesc(equipmentId);}
}
