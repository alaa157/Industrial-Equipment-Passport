package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="fault_reports",indexes={
@Index(name="idx_fault_equipment",columnList="equipment_id"),
@Index(name="idx_fault_status",columnList="status"),
@Index(name="idx_fault_severity",columnList="severity")
})
public class FaultReport {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@ManyToOne(fetch=FetchType.EAGER,optional=false)
@JoinColumn(name="equipment_id",nullable=false)
private Equipment equipment;

@Column(nullable=false)
private UUID reporterId;

@Column(nullable=false,length=180)
private String title;

@Column(nullable=false,length=3000)
private String description;

@Enumerated(EnumType.STRING)
@Column(nullable=false,length=20)
private FaultSeverity severity;

@Column(nullable=false)
private Instant reportedAt=Instant.now();

@Enumerated(EnumType.STRING)
@Column(nullable=false,length=40)
private FaultStatus status=FaultStatus.OPEN;

@Column
private UUID maintenanceRequestId;

protected FaultReport(){}

public FaultReport(Equipment equipment,UUID reporterId,String title,String description,FaultSeverity severity){
this.equipment=equipment;
this.reporterId=reporterId;
this.title=title;
this.description=description;
this.severity=severity;
}

public UUID getId(){return id;}
public Equipment getEquipment(){return equipment;}
public UUID getReporterId(){return reporterId;}
public String getTitle(){return title;}
public String getDescription(){return description;}
public FaultSeverity getSeverity(){return severity;}
public Instant getReportedAt(){return reportedAt;}
public FaultStatus getStatus(){return status;}
public UUID getMaintenanceRequestId(){return maintenanceRequestId;}
public void convertToMaintenance(UUID id){this.maintenanceRequestId=id;this.status=FaultStatus.CONVERTED_TO_MAINTENANCE;}
public void resolve(){status=FaultStatus.RESOLVED;}
}
