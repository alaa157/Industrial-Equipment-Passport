package com.industrial.equipment.service;

import com.industrial.equipment.dto.SparePartRequest;
import com.industrial.equipment.entity.*;
import com.industrial.equipment.repository.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SparePartService {
private final SparePartRepository parts;
private final EquipmentRepository equipment;
private final EquipmentPartRepository equipmentParts;
private final PartReplacementRepository replacements;
private final EventPublisher events;

public SparePartService(SparePartRepository parts,EquipmentRepository equipment,EquipmentPartRepository equipmentParts,PartReplacementRepository replacements,EventPublisher events){
this.parts=parts;this.equipment=equipment;this.equipmentParts=equipmentParts;this.replacements=replacements;this.events=events;
}

@Transactional
public SparePart create(SparePartRequest r){
return parts.save(new SparePart(r.partNumber(),r.name(),r.description(),r.manufacturer(),r.quantity(),r.minimumQuantity(),r.unitCost(),r.location()));
}

@Transactional
public PartReplacement consume(UUID partId,int quantity,UUID equipmentId,UUID maintenanceId){
if(quantity<=0)throw new IllegalArgumentException("Quantity must be positive");
SparePart part=parts.findById(partId).orElseThrow(()->new NoSuchElementException("Spare part not found"));
Equipment asset=equipment.findById(equipmentId).orElseThrow(()->new NoSuchElementException("Equipment not found"));
part.decrease(quantity);
parts.save(part);

EquipmentPart installed=equipmentParts.findByEquipmentIdAndPartId(equipmentId,partId).orElse(null);
if(installed==null)equipmentParts.save(new EquipmentPart(asset,part,quantity));
else{
equipmentParts.delete(installed);
equipmentParts.save(new EquipmentPart(asset,part,installed.getQuantityInstalled()+quantity));
}

PartReplacement replacement=replacements.save(new PartReplacement(asset,part,maintenanceId,quantity));
events.publish("PartReplacedEvent",Map.of(
"partId",partId,
"equipmentId",equipmentId,
"maintenanceId",maintenanceId==null?"":maintenanceId,
"quantity",quantity
));

if(part.isLowStock())events.publish("SparePartLowStockEvent",Map.of(
"partId",partId,
"partNumber",part.getPartNumber(),
"quantity",part.getQuantity(),
"minimumQuantity",part.getMinimumQuantity()
));

return replacement;
}

@Transactional(readOnly=true)
public List<SparePart> all(){return parts.findAll(org.springframework.data.domain.Sort.by("name"));}

@Transactional(readOnly=true)
public List<SparePart> lowStock(){
return parts.findAll().stream().filter(SparePart::isLowStock).sorted(java.util.Comparator.comparing(SparePart::getQuantity)).toList();
}

@Transactional(readOnly=true)
public List<EquipmentPart> equipmentParts(UUID equipmentId){return equipmentParts.findByEquipmentId(equipmentId);}

@Transactional(readOnly=true)
public List<PartReplacement> replacements(UUID equipmentId){return replacements.findByEquipmentIdOrderByReplacedAtDesc(equipmentId);}
}
