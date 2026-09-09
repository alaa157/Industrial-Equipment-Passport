package com.industrial.maintenance.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="maintenance_attachments")
public class MaintenanceAttachment {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(nullable=false)
private UUID maintenanceId;

@Column(nullable=false,length=255)
private String originalFilename;

@Column(nullable=false,length=255,unique=true)
private String storedFilename;

@Column(nullable=false,length=120)
private String contentType;

@Column(nullable=false)
private long sizeBytes;

@Column(nullable=false,length=500)
private String relativePath;

@Column(nullable=false)
private Instant createdAt=Instant.now();

protected MaintenanceAttachment(){}

public MaintenanceAttachment(UUID maintenanceId,String originalFilename,String storedFilename,String contentType,long sizeBytes,String relativePath){
this.maintenanceId=maintenanceId;
this.originalFilename=originalFilename;
this.storedFilename=storedFilename;
this.contentType=contentType;
this.sizeBytes=sizeBytes;
this.relativePath=relativePath;
}

public UUID getId(){return id;}
public UUID getMaintenanceId(){return maintenanceId;}
public String getOriginalFilename(){return originalFilename;}
public String getStoredFilename(){return storedFilename;}
public String getContentType(){return contentType;}
public long getSizeBytes(){return sizeBytes;}
public String getRelativePath(){return relativePath;}
public Instant getCreatedAt(){return createdAt;}
}
