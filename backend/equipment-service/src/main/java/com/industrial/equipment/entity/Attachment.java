package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="attachments",indexes={
@Index(name="idx_attachment_equipment",columnList="equipment_id")
})
public class Attachment {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@ManyToOne(fetch=FetchType.LAZY,optional=false)
@JoinColumn(name="equipment_id",nullable=false)
private Equipment equipment;

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

protected Attachment(){}
public Attachment(Equipment equipment,String originalFilename,String storedFilename,String contentType,long sizeBytes,String relativePath){
this.equipment=equipment;
this.originalFilename=originalFilename;
this.storedFilename=storedFilename;
this.contentType=contentType;
this.sizeBytes=sizeBytes;
this.relativePath=relativePath;
}

public UUID getId(){return id;}
public String getOriginalFilename(){return originalFilename;}
public String getStoredFilename(){return storedFilename;}
public String getContentType(){return contentType;}
public long getSizeBytes(){return sizeBytes;}
public String getRelativePath(){return relativePath;}
public Instant getCreatedAt(){return createdAt;}
public Equipment getEquipment(){return equipment;}
}