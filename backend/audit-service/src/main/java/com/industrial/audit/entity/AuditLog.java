package com.industrial.audit.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="audit_logs",indexes={
@Index(name="idx_audit_timestamp",columnList="timestamp"),
@Index(name="idx_audit_entity",columnList="entity_type,entity_id"),
@Index(name="idx_audit_user",columnList="user_id")
})
public class AuditLog {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

private UUID userId;

@Column(nullable=false,length=120)
private String action;

@Column(nullable=false,length=80)
private String entityType;

private UUID entityId;

@Column(nullable=false)
private Instant timestamp=Instant.now();

@Column(length=64)
private String ipAddress;

@Column(length=100)
private String correlationId;

@Column(columnDefinition="TEXT")
private String oldValues;

@Column(columnDefinition="TEXT")
private String newValues;

protected AuditLog(){}

public AuditLog(UUID userId,String action,String entityType,UUID entityId,String ipAddress,String correlationId,String oldValues,String newValues){
this.userId=userId;this.action=action;this.entityType=entityType;this.entityId=entityId;this.ipAddress=ipAddress;this.correlationId=correlationId;this.oldValues=oldValues;this.newValues=newValues;
}

public UUID getId(){return id;}
public UUID getUserId(){return userId;}
public String getAction(){return action;}
public String getEntityType(){return entityType;}
public UUID getEntityId(){return entityId;}
public Instant getTimestamp(){return timestamp;}
public String getIpAddress(){return ipAddress;}
public String getCorrelationId(){return correlationId;}
public String getOldValues(){return oldValues;}
public String getNewValues(){return newValues;}
}
