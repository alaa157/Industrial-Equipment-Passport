package com.industrial.maintenance.dto;

import com.industrial.maintenance.entity.*;
import java.time.*;
import java.util.UUID;

public record MaintenanceResponse(
UUID id,
UUID equipmentId,
UUID requesterId,
String title,
String description,
MaintenanceType type,
MaintenanceStatus status,
MaintenancePriority priority,
UUID assignedTechnicianId,
Integer estimatedDurationMinutes,
Integer actualDurationMinutes,
java.math.BigDecimal maintenanceCost,
String laborNotes,
String completionNotes,
LocalDate dueDate,
Instant createdAt,
Instant updatedAt
){
public static MaintenanceResponse from(MaintenanceRequest m){
return new MaintenanceResponse(m.getId(),m.getEquipmentId(),m.getRequesterId(),m.getTitle(),m.getDescription(),m.getType(),m.getStatus(),m.getPriority(),m.getAssignedTechnicianId(),m.getEstimatedDurationMinutes(),m.getActualDurationMinutes(),m.getMaintenanceCost(),m.getLaborNotes(),m.getCompletionNotes(),m.getDueDate(),m.getCreatedAt(),m.getUpdatedAt());
}
}
