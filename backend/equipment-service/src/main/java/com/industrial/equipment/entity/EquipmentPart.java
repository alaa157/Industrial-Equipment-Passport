package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="equipment_parts",uniqueConstraints=@UniqueConstraint(name="uk_equipment_part",columnNames={"equipment_id","part_id"}))
public class EquipmentPart {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@ManyToOne(fetch=FetchType.LAZY,optional=false)
@JoinColumn(name="equipment_id",nullable=false)
private Equipment equipment;

@ManyToOne(fetch=FetchType.EAGER,optional=false)
@JoinColumn(name="part_id",nullable=false)
private SparePart part;

@Column(nullable=false)
private int quantityInstalled;

protected EquipmentPart(){}
public EquipmentPart(Equipment equipment,SparePart part,int quantityInstalled){
this.equipment=equipment;
this.part=part;
this.quantityInstalled=quantityInstalled;
}
public UUID getId(){return id;}
public Equipment getEquipment(){return equipment;}
public SparePart getPart(){return part;}
public int getQuantityInstalled(){return quantityInstalled;}
}
