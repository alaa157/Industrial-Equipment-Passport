package com.industrial.equipment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="spare_parts",indexes={
@Index(name="idx_part_number",columnList="part_number"),
@Index(name="idx_part_quantity",columnList="quantity")
})
public class SparePart {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@Column(name="part_number",nullable=false,length=80,unique=true)
private String partNumber;

@Column(nullable=false,length=180)
private String name;

@Column(length=1000)
private String description;

@Column(length=150)
private String manufacturer;

@Column(nullable=false)
private int quantity;

@Column(nullable=false)
private int minimumQuantity;

@Column(nullable=false,precision=14,scale=2)
private BigDecimal unitCost;

@Column(length=150)
private String location;

@Column(nullable=false)
private Instant createdAt=Instant.now();

@Column(nullable=false)
private Instant updatedAt=Instant.now();

@Version
private long version;

protected SparePart(){}

public SparePart(String partNumber,String name,String description,String manufacturer,int quantity,int minimumQuantity,BigDecimal unitCost,String location){
this.partNumber=partNumber;
this.name=name;
this.description=description;
this.manufacturer=manufacturer;
this.quantity=quantity;
this.minimumQuantity=minimumQuantity;
this.unitCost=unitCost;
this.location=location;
}

@PreUpdate void touch(){updatedAt=Instant.now();}
public UUID getId(){return id;}
public String getPartNumber(){return partNumber;}
public String getName(){return name;}
public String getDescription(){return description;}
public String getManufacturer(){return manufacturer;}
public int getQuantity(){return quantity;}
public int getMinimumQuantity(){return minimumQuantity;}
public BigDecimal getUnitCost(){return unitCost;}
public String getLocation(){return location;}
public void decrease(int amount){
if(amount<=0)throw new IllegalArgumentException("Quantity must be positive");
if(quantity<amount)throw new IllegalStateException("Insufficient spare-part inventory");
quantity-=amount;
}
public boolean isLowStock(){return quantity<=minimumQuantity;}
}
