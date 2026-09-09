package com.industrial.maintenance.entity;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name="maintenance_schedules",indexes=@Index(name="idx_schedule_next_run",columnList="next_run_date"))
public class MaintenanceSchedule {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(nullable=false)
private UUID equipmentId;

@Column(nullable=false, length=180)
private String title;

@Column(nullable=false)
private int intervalDays;

@Column(nullable=false)
private LocalDate nextRunDate;

@Column(nullable=false)
private boolean active=true;

@Enumerated(EnumType.STRING)
@Column(nullable=false)
private MaintenancePriority priority=MaintenancePriority.MEDIUM;

protected MaintenanceSchedule(){}

public MaintenanceSchedule(UUID equipmentId,String title,int intervalDays,LocalDate nextRunDate,MaintenancePriority priority){
this.equipmentId=equipmentId;this.title=title;this.intervalDays=intervalDays;this.nextRunDate=nextRunDate;this.priority=priority;
}

public UUID getId(){return id;}
public UUID getEquipmentId(){return equipmentId;}
public String getTitle(){return title;}
public int getIntervalDays(){return intervalDays;}
public LocalDate getNextRunDate(){return nextRunDate;}
public boolean isActive(){return active;}
public MaintenancePriority getPriority(){return priority;}

public void advance(){nextRunDate=nextRunDate.plusDays(intervalDays);}
}
