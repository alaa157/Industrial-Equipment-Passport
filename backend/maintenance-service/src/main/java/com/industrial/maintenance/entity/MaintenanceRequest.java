package com.industrial.maintenance.entity;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name="maintenance_requests",indexes={
@Index(name="idx_maintenance_equipment",columnList="equipment_id"),
@Index(name="idx_maintenance_status",columnList="status"),
@Index(name="idx_maintenance_priority",columnList="priority"),
@Index(name="idx_maintenance_due",columnList="due_date")
})
public class MaintenanceRequest {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(nullable=false)
private UUID equipmentId;

@Column(nullable=false)
private UUID requesterId;

@Column(length=180)
private String title;

@Column(nullable=false,length=3000)
private String description;

@Enumerated(EnumType.STRING)
@Column(nullable=false,length=30)
private MaintenanceType type;

@Enumerated(EnumType.STRING)
@Column(nullable=false,length=20)
private MaintenanceStatus status=MaintenanceStatus.OPEN;

@Enumerated(EnumType.STRING)
@Column(nullable=false,length=20)
private MaintenancePriority priority=MaintenancePriority.MEDIUM;

@Column
private UUID assignedTechnicianId;

@Column
private Integer estimatedDurationMinutes;

@Column
private Integer actualDurationMinutes;

@Column(length=3000)
private String laborNotes;

@Column(precision=14,scale=2)
private java.math.BigDecimal maintenanceCost;

@Column(length=2000)
private String completionNotes;

@Column
private LocalDate dueDate;

@Column(nullable=false)
private Instant createdAt=Instant.now();

@Column(nullable=false)
private Instant updatedAt=Instant.now();

protected MaintenanceRequest(){}

public MaintenanceRequest(UUID equipmentId,UUID requesterId,String title,String description,MaintenanceType type,MaintenancePriority priority,LocalDate dueDate){
this.equipmentId=equipmentId;this.requesterId=requesterId;this.title=title;this.description=description;this.type=type;this.priority=priority;this.dueDate=dueDate;
}

@PreUpdate void touch(){updatedAt=Instant.now();}

public UUID getId(){return id;}
public UUID getEquipmentId(){return equipmentId;}
public UUID getRequesterId(){return requesterId;}
public String getTitle(){return title;}
public String getDescription(){return description;}
public MaintenanceType getType(){return type;}
public MaintenanceStatus getStatus(){return status;}
public MaintenancePriority getPriority(){return priority;}
public UUID getAssignedTechnicianId(){return assignedTechnicianId;}
public Integer getEstimatedDurationMinutes(){return estimatedDurationMinutes;}
public Integer getActualDurationMinutes(){return actualDurationMinutes;}
public String getLaborNotes(){return laborNotes;}
public java.math.BigDecimal getMaintenanceCost(){return maintenanceCost;}
public String getCompletionNotes(){return completionNotes;}
public LocalDate getDueDate(){return dueDate;}
public Instant getCreatedAt(){return createdAt;}
public Instant getUpdatedAt(){return updatedAt;}

public void assign(UUID technician){assignedTechnicianId=technician;status=MaintenanceStatus.ASSIGNED;}
public void start(){status=MaintenanceStatus.IN_PROGRESS;}
public void hold(){status=MaintenanceStatus.ON_HOLD;}
public void cancel(){status=MaintenanceStatus.CANCELLED;}
public void complete(Integer actualDuration,java.math.BigDecimal cost,String laborNotes,String completionNotes){
actualDurationMinutes=actualDuration;maintenanceCost=cost;this.laborNotes=laborNotes;this.completionNotes=completionNotes;status=MaintenanceStatus.COMPLETED;
}
}
