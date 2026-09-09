package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="inspections",indexes={
@Index(name="idx_inspection_equipment",columnList="equipment_id"),
@Index(name="idx_inspection_date",columnList="inspection_date")
})
public class Inspection {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@ManyToOne(fetch=FetchType.EAGER,optional=false)
@JoinColumn(name="equipment_id",nullable=false)
private Equipment equipment;

@Column(nullable=false)
private UUID inspectorId;

@Column(name="inspection_date",nullable=false)
private LocalDate inspectionDate;

@Column(nullable=false,length=1200)
private String checklist;

@Enumerated(EnumType.STRING)
@Column(nullable=false,length=30)
private InspectionResult result;

@Column(length=2000)
private String notes;

@Column
private LocalDate nextInspectionDate;

@Column(nullable=false)
private Instant createdAt=Instant.now();

protected Inspection(){}
public Inspection(Equipment equipment,UUID inspectorId,LocalDate inspectionDate,String checklist,InspectionResult result,String notes,LocalDate nextInspectionDate){
this.equipment=equipment;
this.inspectorId=inspectorId;
this.inspectionDate=inspectionDate;
this.checklist=checklist;
this.result=result;
this.notes=notes;
this.nextInspectionDate=nextInspectionDate;
}
public UUID getId(){return id;}
public Equipment getEquipment(){return equipment;}
public UUID getInspectorId(){return inspectorId;}
public LocalDate getInspectionDate(){return inspectionDate;}
public String getChecklist(){return checklist;}
public InspectionResult getResult(){return result;}
public String getNotes(){return notes;}
public LocalDate getNextInspectionDate(){return nextInspectionDate;}
public Instant getCreatedAt(){return createdAt;}
}
