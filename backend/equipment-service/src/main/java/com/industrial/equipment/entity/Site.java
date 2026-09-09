package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="sites",uniqueConstraints=@UniqueConstraint(name="uk_site_code",columnNames="code"))
public class Site {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(nullable=false,length=30)
private String code;

@Column(nullable=false,length=150)
private String name;

@Column(length=500)
private String address;

@Column(nullable=false)
private boolean active=true;

protected Site(){}
public Site(String code,String name,String address){this.code=code;this.name=name;this.address=address;}
public UUID getId(){return id;}
public String getCode(){return code;}
public String getName(){return name;}
public String getAddress(){return address;}
public boolean isActive(){return active;}
}
