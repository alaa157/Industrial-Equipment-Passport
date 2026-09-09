package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="areas",uniqueConstraints=@UniqueConstraint(name="uk_area_building_code",columnNames={"building_id","code"}))
public class Area {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@ManyToOne(fetch=FetchType.LAZY,optional=false)
@JoinColumn(name="building_id",nullable=false)
private Building building;

@Column(nullable=false,length=30)
private String code;

@Column(nullable=false,length=150)
private String name;

protected Area(){}
public Area(Building building,String code,String name){this.building=building;this.code=code;this.name=name;}
public UUID getId(){return id;}
public Building getBuilding(){return building;}
public String getCode(){return code;}
public String getName(){return name;}
}
