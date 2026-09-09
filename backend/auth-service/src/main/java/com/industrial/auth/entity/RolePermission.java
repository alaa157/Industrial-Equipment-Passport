package com.industrial.auth.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="role_permissions",uniqueConstraints=@UniqueConstraint(name="uk_role_permission",columnNames={"role","permission_id"}))
public class RolePermission {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Enumerated(EnumType.STRING)
@Column(nullable=false,length=30)
private Role role;

@ManyToOne(fetch=FetchType.EAGER,optional=false)
@JoinColumn(name="permission_id",nullable=false)
private Permission permission;

protected RolePermission(){}

public RolePermission(Role role,Permission permission){
this.role=role;
this.permission=permission;
}

public UUID getId(){return id;}
public Role getRole(){return role;}
public Permission getPermission(){return permission;}
}
