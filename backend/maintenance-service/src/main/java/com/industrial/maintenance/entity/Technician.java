package com.industrial.maintenance.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="technicians",uniqueConstraints=@UniqueConstraint(name="uk_technician_user",columnNames="user_id"))
public class Technician {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(name="user_id",nullable=false)
private UUID userId;

@Column(nullable=false,length=150)
private String name;

@Column(length=120)
private String specialization;

@Column(length=50)
private String phone;

@Column(nullable=false)
private boolean active=true;

protected Technician(){}

public Technician(UUID userId,String name,String specialization,String phone){
this.userId=userId;this.name=name;this.specialization=specialization;this.phone=phone;
}

public UUID getId(){return id;}
public UUID getUserId(){return userId;}
public String getName(){return name;}
public String getSpecialization(){return specialization;}
public String getPhone(){return phone;}
public boolean isActive(){return active;}
}
