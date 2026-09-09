package com.industrial.auth.security;

import com.industrial.auth.entity.*;
import com.industrial.auth.repository.RolePermissionRepository;
import java.time.*;
import java.time.temporal.ChronoUnit;

import javax.crypto.SecretKey;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
private final JwtEncoder encoder;
private final RolePermissionRepository rolePermissions;
private final long accessMinutes;

public JwtService(JwtEncoder encoder,RolePermissionRepository rolePermissions,@Value("${jwt.access-expiration-minutes:15}") long accessMinutes){
this.encoder=encoder;
this.rolePermissions=rolePermissions;
this.accessMinutes=accessMinutes;
}

public String createAccessToken(User user){
Instant now=Instant.now();
List<String> roles=user.getRoles().stream().map(Enum::name).sorted().toList();
List<String> permissions=rolePermissions.findByRoleIn(user.getRoles()).stream()
.map(x->x.getPermission().getCode()).distinct().sorted().toList();

JwtClaimsSet claims=JwtClaimsSet.builder()
.issuer("industrial-equipment-passport")
.subject(user.getId().toString())
.issuedAt(now)
.expiresAt(now.plus(accessMinutes,ChronoUnit.MINUTES))
.claim("username",user.getUsername())
.claim("roles",roles)
.claim("permissions",permissions)
.build();

return encoder.encode(JwtEncoderParameters.from(
JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();
}

public long getAccessExpirationSeconds(){return accessMinutes*60;}
}
