package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="equipment",indexes={
@Index(name="idx_equipment_asset_code",columnList="asset_code"),
@Index(name="idx_equipment_serial",columnList="serial_number"),
@Index(name="idx_equipment_status",columnList="status"),
@Index(name="idx_equipment_criticality",columnList="criticality"),
@Index(name="idx_equipment_site",columnList="site_id")
})
public class Equipment {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(name="asset_code",nullable=false,length=60,unique=true)
private String assetCode;

@Column(name="serial_number",nullable=false,length=120,unique=true)
private String serialNumber;

@Column(nullable=false,length=180)
private String name;

@Column(length=1000)
private String description;

@Column(nullable=false,length=150)
private String manufacturer;

@Column(nullable=false,length=150)
private String model;

@ManyToOne(fetch=FetchType.EAGER,optional=false)
@JoinColumn(name="equipment_type_id",nullable=false)
private EquipmentType equipmentType;

@Column
private LocalDate purchaseDate;

@Column
private LocalDate installationDate;

@Column
private LocalDate warrantyExpiration;

@Enumerated(EnumType.STRING)
@Column(nullable=false,length=30)
private EquipmentStatus status=EquipmentStatus.ACTIVE;

@Enumerated(EnumType.STRING)
@Column(nullable=false,length=20)
private Criticality criticality=Criticality.MEDIUM;

@ManyToOne(fetch=FetchType.EAGER)
@JoinColumn(name="site_id")
private Site site;

@ManyToOne(fetch=FetchType.EAGER)
@JoinColumn(name="building_id")
private Building building;

@ManyToOne(fetch=FetchType.EAGER)
@JoinColumn(name="area_id")
private Area area;

@Column(length=120)
private String responsibleDepartment;

@Column
private UUID responsibleTechnicianId;

@Column(length=2000)
private String notes;

@Column(nullable=false)
private Instant createdAt=Instant.now();

@Column(nullable=false)
private Instant updatedAt=Instant.now();

@Version
private long version;

protected Equipment(){}

public Equipment(String assetCode,String serialNumber,String name,String description,String manufacturer,String model,EquipmentType equipmentType,LocalDate purchaseDate,LocalDate installationDate,LocalDate warrantyExpiration,Criticality criticality,Site site,Building building,Area area,String responsibleDepartment,UUID responsibleTechnicianId,String notes){
this.assetCode=assetCode;
this.serialNumber=serialNumber;
this.name=name;
this.description=description;
this.manufacturer=manufacturer;
this.model=model;
this.equipmentType=equipmentType;
this.purchaseDate=purchaseDate;
this.installationDate=installationDate;
this.warrantyExpiration=warrantyExpiration;
this.criticality=criticality;
this.site=site;
this.building=building;
this.area=area;
this.responsibleDepartment=responsibleDepartment;
this.responsibleTechnicianId=responsibleTechnicianId;
this.notes=notes;
}

@PreUpdate void touch(){updatedAt=Instant.now();}

public UUID getId(){return id;}
public String getAssetCode(){return assetCode;}
public String getSerialNumber(){return serialNumber;}
public String getName(){return name;}
public String getDescription(){return description;}
public String getManufacturer(){return manufacturer;}
public String getModel(){return model;}
public EquipmentType getEquipmentType(){return equipmentType;}
public LocalDate getPurchaseDate(){return purchaseDate;}
public LocalDate getInstallationDate(){return installationDate;}
public LocalDate getWarrantyExpiration(){return warrantyExpiration;}
public EquipmentStatus getStatus(){return status;}
public Criticality getCriticality(){return criticality;}
public Site getSite(){return site;}
public Building getBuilding(){return building;}
public Area getArea(){return area;}
public String getResponsibleDepartment(){return responsibleDepartment;}
public UUID getResponsibleTechnicianId(){return responsibleTechnicianId;}
public String getNotes(){return notes;}
public Instant getCreatedAt(){return createdAt;}
public Instant getUpdatedAt(){return updatedAt;}
public void setStatus(EquipmentStatus status){this.status=status;}
}
