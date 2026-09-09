package com.industrial.notification.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="notifications",indexes={
@Index(name="idx_notification_user",columnList="user_id"),
@Index(name="idx_notification_read",columnList="read_at")
})
public class Notification {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

private UUID userId;

@Column(nullable=false,length=150)
private String title;

@Column(nullable=false,length=1000)
private String message;

@Column(nullable=false,length=80)
private String eventType;

private UUID entityId;

@Column(nullable=false)
private Instant createdAt=Instant.now();

private Instant readAt;

protected Notification(){}

public Notification(UUID userId,String title,String message,String eventType,UUID entityId){
this.userId=userId;this.title=title;this.message=message;this.eventType=eventType;this.entityId=entityId;
}

public UUID getId(){return id;}
public UUID getUserId(){return userId;}
public String getTitle(){return title;}
public String getMessage(){return message;}
public String getEventType(){return eventType;}
public UUID getEntityId(){return entityId;}
public Instant getCreatedAt(){return createdAt;}
public Instant getReadAt(){return readAt;}
public void markRead(){readAt=Instant.now();}
}
