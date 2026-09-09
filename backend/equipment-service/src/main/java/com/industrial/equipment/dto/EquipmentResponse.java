package com.industrial.equipment.dto;

import com.industrial.equipment.entity.*;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

public record EquipmentResponse(
UUID id,
String assetCode,
String serialNumber,
String name,
String description,
String manufacturer,
String model,
UUID equipmentTypeId,
String equipmentType,
LocalDate purchaseDate,
LocalDate installationDate,
LocalDate warrantyExpiration,
EquipmentStatus status,
Criticality criticality,
UUID siteId,
String site,
UUID buildingId,
String building,
UUID areaId,
String area,
String responsibleDepartment,
UUID responsibleTechnicianId,
String notes,
Instant createdAt,
Instant updatedAt
){
public static EquipmentResponse from(Equipment e){
return new EquipmentResponse(
e.getId(),e.getAssetCode(),e.getSerialNumber(),e.getName(),e.getDescription(),e.getManufacturer(),e.getModel(),
e.getEquipmentType().getId(),e.getEquipmentType().getName(),e.getPurchaseDate(),e.getInstallationDate(),e.getWarrantyExpiration(),
e.getStatus(),e.getCriticality(),
e.getSite()==null?null:e.getSite().getId(),e.getSite()==null?null:e.getSite().getName(),
e.getBuilding()==null?null:e.getBuilding().getId(),e.getBuilding()==null?null:e.getBuilding().getName(),
e.getArea()==null?null:e.getArea().getId(),e.getArea()==null?null:e.getArea().getName(),
e.getResponsibleDepartment(),e.getResponsibleTechnicianId(),e.getNotes(),e.getCreatedAt(),e.getUpdatedAt()
);
}
}
