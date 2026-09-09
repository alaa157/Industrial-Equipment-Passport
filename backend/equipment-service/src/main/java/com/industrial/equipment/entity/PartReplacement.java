package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="part_replacements",indexes={
@Index(name="idx_part_replacement_equipment",columnList="equipment_id"),
@Index(name="idx_part_replacement_maintenance",columnList="maintenance_id")
})
public class PartReplacement {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@ManyToOne(fetch=FetchType.EAGER,optional=false)
@JoinColumn(name="equipment_id",nullable=false)
private Equipment equipment;

@ManyToOne(fetch=FetchType.EAGER,optional=false)
@JoinColumn(name="part_id",nullable=false)
private SparePart part;

private UUID maintenanceId;

@Column(nullable=false)
private int quantity;

@Column(nullable=false)
private Instant replacedAt=Instant.now();

protected PartReplacement(){}

public PartReplacement(Equipment equipment,SparePart part,UUID maintenanceId,int quantity){
this.equipment=equipment;
this.part=part;
this.maintenanceId=maintenanceId;
this.quantity=quantity;
}

public UUID getId(){return id;}
public Equipment getEquipment(){return equipment;}
public SparePart getPart(){return part;}
public UUID getMaintenanceId(){return maintenanceId;}
public int getQuantity(){return quantity;}
public Instant getReplacedAt(){return replacedAt;}
}
