package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="buildings",uniqueConstraints=@UniqueConstraint(name="uk_building_site_code",columnNames={"site_id","code"}))
public class Building {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@ManyToOne(fetch=FetchType.LAZY,optional=false)
@JoinColumn(name="site_id",nullable=false)
private Site site;

@Column(nullable=false,length=30)
private String code;

@Column(nullable=false,length=150)
private String name;

protected Building(){}
public Building(Site site,String code,String name){this.site=site;this.code=code;this.name=name;}
public UUID getId(){return id;}
public Site getSite(){return site;}
public String getCode(){return code;}
public String getName(){return name;}
}
