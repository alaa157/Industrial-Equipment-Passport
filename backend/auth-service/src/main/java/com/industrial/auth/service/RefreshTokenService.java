package com.industrial.auth.service;

import com.industrial.auth.entity.RefreshToken;
import com.industrial.auth.entity.User;
import com.industrial.auth.repository.RefreshTokenRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
private final RefreshTokenRepository repository;
private final long expirationDays;
private final SecureRandom random=new SecureRandom();

public RefreshTokenService(RefreshTokenRepository repository,@Value("${jwt.refresh-expiration-days:7}") long expirationDays){
this.repository=repository;
this.expirationDays=expirationDays;
}

@Transactional
public String issue(User user){
byte[] bytes=new byte[64];
random.nextBytes(bytes);
String raw=java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
repository.save(new RefreshToken(user,hash(raw),Instant.now().plus(expirationDays,ChronoUnit.DAYS)));
return raw;
}

@Transactional
public User validateAndRevoke(String raw){
RefreshToken token=repository.findByTokenHash(hash(raw)).orElseThrow(()->new IllegalArgumentException("Invalid refresh token"));
if(!token.isActive()) throw new IllegalArgumentException("Refresh token is expired or revoked");
token.revoke();
return token.getUser();
}

@Transactional
public void revoke(String raw){
repository.findByTokenHash(hash(raw)).ifPresent(RefreshToken::revoke);
}

private String hash(String value){
try{
byte[] digest=MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
StringBuilder result=new StringBuilder();
for(byte b:digest) result.append(String.format("%02x",b));
return result.toString();
}catch(NoSuchAlgorithmException ex){throw new IllegalStateException(ex);}
}
}
