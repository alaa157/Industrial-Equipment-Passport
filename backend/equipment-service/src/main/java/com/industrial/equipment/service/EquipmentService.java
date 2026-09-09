package com.industrial.equipment.service;

import com.industrial.equipment.dto.*;
import com.industrial.equipment.entity.*;
import com.industrial.equipment.repository.*;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipmentService {
private final EquipmentRepository equipment;
private final EquipmentTypeRepository types;
private final SiteRepository sites;
private final BuildingRepository buildings;
private final AreaRepository areas;
private final EventPublisher events;

public EquipmentService(EquipmentRepository equipment,EquipmentTypeRepository types,SiteRepository sites,BuildingRepository buildings,AreaRepository areas,EventPublisher events){
this.equipment=equipment;this.types=types;this.sites=sites;this.buildings=buildings;this.areas=areas;this.events=events;
}

@Transactional
public EquipmentResponse create(EquipmentRequest r){
if(equipment.existsByAssetCodeIgnoreCase(r.assetCode()))throw new IllegalArgumentException("Asset code already exists");
if(equipment.existsBySerialNumberIgnoreCase(r.serialNumber()))throw new IllegalArgumentException("Serial number already exists");
EquipmentType type=types.findById(r.equipmentTypeId()).orElseThrow(()->new IllegalArgumentException("Equipment type not found"));
Site site=r.siteId()==null?null:sites.findById(r.siteId()).orElseThrow(()->new IllegalArgumentException("Site not found"));
Building building=r.buildingId()==null?null:buildings.findById(r.buildingId()).orElseThrow(()->new IllegalArgumentException("Building not found"));
Area area=r.areaId()==null?null:areas.findById(r.areaId()).orElseThrow(()->new IllegalArgumentException("Area not found"));
Equipment e=new Equipment(r.assetCode(),r.serialNumber(),r.name(),r.description(),r.manufacturer(),r.model(),type,r.purchaseDate(),r.installationDate(),r.warrantyExpiration(),r.criticality(),site,building,area,r.responsibleDepartment(),r.responsibleTechnicianId(),r.notes());
Equipment saved=equipment.save(e);
events.publish("EquipmentCreatedEvent",Map.of("equipmentId",saved.getId(),"assetCode",saved.getAssetCode(),"name",saved.getName()));
return EquipmentResponse.from(saved);
}

@Transactional(readOnly=true)
public Page<EquipmentResponse> search(String query,EquipmentStatus status,Criticality criticality,UUID siteId,int page,int size,String sort,String direction){
String property=Set.of("name","assetCode","manufacturer","createdAt","status","criticality").contains(sort)?sort:"createdAt";
Sort.Direction dir="asc".equalsIgnoreCase(direction)?Sort.Direction.ASC:Sort.Direction.DESC;
Specification<Equipment> specification=(root,q,cb)->{
List<Predicate> predicates=new ArrayList<>();
if(query!=null&&!query.isBlank()){
String like="%"+query.trim().toLowerCase()+"%";
predicates.add(cb.or(
cb.like(cb.lower(root.get("name")),like),
cb.like(cb.lower(root.get("assetCode")),like),
cb.like(cb.lower(root.get("serialNumber")),like),
cb.like(cb.lower(root.get("manufacturer")),like),
cb.like(cb.lower(root.get("model")),like)
));
}
if(status!=null)predicates.add(cb.equal(root.get("status"),status));
if(criticality!=null)predicates.add(cb.equal(root.get("criticality"),criticality));
if(siteId!=null)predicates.add(cb.equal(root.get("site").get("id"),siteId));
return cb.and(predicates.toArray(new Predicate[0]));
};
return equipment.findAll(specification,PageRequest.of(Math.max(0,page),Math.min(Math.max(1,size),100),Sort.by(dir,property))).map(EquipmentResponse::from);
}

@Transactional(readOnly=true)
public Equipment get(UUID id){return equipment.findById(id).orElseThrow(()->new NoSuchElementException("Equipment not found"));}

@Transactional
public EquipmentResponse changeStatus(UUID id,EquipmentStatus next){
Equipment e=get(id);
EquipmentStatus current=e.getStatus();
if(!allowed(current,next))throw new IllegalArgumentException("Invalid equipment status transition: "+current+" -> "+next);
e.setStatus(next);
Equipment saved=equipment.save(e);
events.publish("EquipmentStatusChangedEvent",Map.of("equipmentId",saved.getId(),"oldStatus",current.name(),"newStatus",next.name()));
return EquipmentResponse.from(saved);
}

private boolean allowed(EquipmentStatus from,EquipmentStatus to){
if(from==to)return true;
return switch(from){
case ACTIVE->Set.of(EquipmentStatus.UNDER_MAINTENANCE,EquipmentStatus.OUT_OF_SERVICE,EquipmentStatus.RETIRED).contains(to);
case UNDER_MAINTENANCE->Set.of(EquipmentStatus.ACTIVE,EquipmentStatus.OUT_OF_SERVICE).contains(to);
case OUT_OF_SERVICE->Set.of(EquipmentStatus.UNDER_MAINTENANCE,EquipmentStatus.ACTIVE,EquipmentStatus.RETIRED).contains(to);
case RETIRED->Set.of(EquipmentStatus.DECOMMISSIONED).contains(to);
case DECOMMISSIONED->false;
};
}

@Transactional(readOnly=true)
public Map<String,Object> dashboard(){
return Map.of(
"totalEquipment",equipment.count(),
"activeEquipment",equipment.countByStatus(EquipmentStatus.ACTIVE),
"underMaintenance",equipment.countByStatus(EquipmentStatus.UNDER_MAINTENANCE),
"outOfService",equipment.countByStatus(EquipmentStatus.OUT_OF_SERVICE),
"retired",equipment.countByStatus(EquipmentStatus.RETIRED)
);
}
}
