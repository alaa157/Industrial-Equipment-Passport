package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="downtime_records",indexes={
@Index(name="idx_downtime_equipment",columnList="equipment_id"),
@Index(name="idx_downtime_start",columnList="start_time")
})
public class DowntimeRecord {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@ManyToOne(fetch=FetchType.EAGER,optional=false)
@JoinColumn(name="equipment_id",nullable=false)
private Equipment equipment;

@Column(nullable=false,length=500)
private String reason;

@Column(name="start_time",nullable=false)
private Instant startTime;

@Column(name="end_time")
private Instant endTime;

@Column(name="related_maintenance_id")
private UUID relatedMaintenanceId;

@Column(length=1000)
private String notes;

protected DowntimeRecord(){}

public DowntimeRecord(Equipment equipment,String reason,Instant startTime,Instant endTime,UUID relatedMaintenanceId,String notes){
this.equipment=equipment;
this.reason=reason;
this.startTime=startTime;
this.endTime=endTime;
this.relatedMaintenanceId=relatedMaintenanceId;
this.notes=notes;
}

public UUID getId(){return id;}
public Equipment getEquipment(){return equipment;}
public String getReason(){return reason;}
public Instant getStartTime(){return startTime;}
public Instant getEndTime(){return endTime;}
public UUID getRelatedMaintenanceId(){return relatedMaintenanceId;}
public String getNotes(){return notes;}
public long getDurationMinutes(){
Instant end=endTime==null?Instant.now():endTime;
return Math.max(0,Duration.between(startTime,end).toMinutes());
}
}
