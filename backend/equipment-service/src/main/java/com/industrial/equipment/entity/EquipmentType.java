package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="equipment_types",uniqueConstraints=@UniqueConstraint(name="uk_equipment_type_name",columnNames="name"))
public class EquipmentType {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(nullable=false,length=100)
private String name;

@Column(length=500)
private String description;

protected EquipmentType(){}
public EquipmentType(String name,String description){this.name=name;this.description=description;}
public UUID getId(){return id;}
public String getName(){return name;}
public String getDescription(){return description;}
}
