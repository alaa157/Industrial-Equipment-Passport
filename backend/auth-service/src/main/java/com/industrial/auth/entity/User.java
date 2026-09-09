package com.industrial.auth.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name="users",uniqueConstraints={
@UniqueConstraint(name="uk_users_email",columnNames="email"),
@UniqueConstraint(name="uk_users_username",columnNames="username")
})
public class User {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(nullable=false,length=80)
private String username;

@Column(nullable=false,length=180)
private String email;

@Column(nullable=false,length=255)
private String passwordHash;

@Column(nullable=false)
private boolean enabled=true;

@Column(nullable=false)
private Instant createdAt=Instant.now();

@Column(nullable=false)
private Instant updatedAt=Instant.now();

@ElementCollection(fetch=FetchType.EAGER)
@CollectionTable(name="user_roles",joinColumns=@JoinColumn(name="user_id"))
@Enumerated(EnumType.STRING)
@Column(name="role",nullable=false,length=30)
private Set<Role> roles=new HashSet<>();

@Version
private long version;

protected User(){}

public User(String username,String email,String passwordHash,Set<Role> roles){
this.username=username;
this.email=email;
this.passwordHash=passwordHash;
this.roles=new HashSet<>(roles);
}

@PreUpdate
void preUpdate(){updatedAt=Instant.now();}

public UUID getId(){return id;}
public String getUsername(){return username;}
public String getEmail(){return email;}
public String getPasswordHash(){return passwordHash;}
public boolean isEnabled(){return enabled;}
public Instant getCreatedAt(){return createdAt;}
public Instant getUpdatedAt(){return updatedAt;}
public Set<Role> getRoles(){return roles;}
public void setRoles(Set<Role> roles){this.roles=new HashSet<>(roles);}
public void setPasswordHash(String hash){passwordHash=hash;}
public void setEnabled(boolean enabled){this.enabled=enabled;}
}
