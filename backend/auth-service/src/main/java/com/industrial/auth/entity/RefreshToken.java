package com.industrial.auth.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="refresh_tokens",indexes={
@Index(name="idx_refresh_token_hash",columnList="token_hash"),
@Index(name="idx_refresh_user",columnList="user_id")
})
public class RefreshToken {
@Id
@GeneratedValue(strategy=GenerationType.UUID)
private UUID id;

@ManyToOne(fetch=FetchType.LAZY,optional=false)
@JoinColumn(name="user_id",nullable=false)
private User user;

@Column(name="token_hash",nullable=false,length=64,unique=true)
private String tokenHash;

@Column(nullable=false)
private Instant expiresAt;

@Column(nullable=false)
private Instant createdAt=Instant.now();

@Column
private Instant revokedAt;

protected RefreshToken(){}

public RefreshToken(User user,String tokenHash,Instant expiresAt){
this.user=user;
this.tokenHash=tokenHash;
this.expiresAt=expiresAt;
}

public UUID getId(){return id;}
public User getUser(){return user;}
public String getTokenHash(){return tokenHash;}
public Instant getExpiresAt(){return expiresAt;}
public Instant getCreatedAt(){return createdAt;}
public Instant getRevokedAt(){return revokedAt;}
public boolean isActive(){return revokedAt==null&&expiresAt.isAfter(Instant.now());}
public void revoke(){revokedAt=Instant.now();}
}
