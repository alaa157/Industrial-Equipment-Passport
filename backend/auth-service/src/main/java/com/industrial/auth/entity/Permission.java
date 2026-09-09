package com.industrial.auth.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="permissions",uniqueConstraints=@UniqueConstraint(name="uk_permission_code",columnNames="code"))
public class Permission {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(nullable=false,length=100)
private String code;

@Column(nullable=false,length=255)
private String description;

protected Permission(){}

public Permission(String code,String description){
this.code=code;
this.description=description;
}

public UUID getId(){return id;}
public String getCode(){return code;}
public String getDescription(){return description;}
}
