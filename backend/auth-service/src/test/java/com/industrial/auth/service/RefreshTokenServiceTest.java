package com.industrial.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.industrial.auth.entity.RefreshToken;
import com.industrial.auth.entity.Role;
import com.industrial.auth.entity.User;
import com.industrial.auth.repository.RefreshTokenRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RefreshTokenServiceTest {
private RefreshTokenRepository repository;
private RefreshTokenService service;

@BeforeEach
void setup(){
repository=mock(RefreshTokenRepository.class);
service=new RefreshTokenService(repository,7);
}

@Test
void issueStoresHashedToken(){
User user=new User("admin","admin@test.local","hash",Set.of(Role.ADMIN));
when(repository.save(any())).thenAnswer(inv->inv.getArgument(0));
String token=service.issue(user);
assertNotNull(token);
assertFalse(token.isBlank());
verify(repository).save(any(RefreshToken.class));
}

@Test
void expiredTokenIsRejected(){
User user=new User("admin","admin@test.local","hash",Set.of(Role.ADMIN));
RefreshToken token=new RefreshToken(user,"abc",Instant.now().minusSeconds(60));
when(repository.findByTokenHash(any())).thenReturn(Optional.of(token));
assertThrows(IllegalArgumentException.class,()->service.validateAndRevoke("token"));
}
}
