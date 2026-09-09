package com.industrial.auth.service;

import com.industrial.auth.dto.*;
import com.industrial.auth.entity.User;
import com.industrial.auth.repository.UserRepository;
import com.industrial.auth.security.JwtService;
import java.util.Objects;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
private final UserRepository users;
private final AuthenticationManager authenticationManager;
private final JwtService jwtService;
private final RefreshTokenService refreshTokens;
private final RateLimitService rateLimiter;

public AuthService(UserRepository users,AuthenticationManager authenticationManager,JwtService jwtService,RefreshTokenService refreshTokens,RateLimitService rateLimiter){
this.users=users;
this.authenticationManager=authenticationManager;
this.jwtService=jwtService;
this.refreshTokens=refreshTokens;
this.rateLimiter=rateLimiter;
}

@Transactional
public AuthResponse login(LoginRequest request){
String identity=request.usernameOrEmail().trim();
if(!rateLimiter.allowLogin(identity)) throw new IllegalStateException("Too many login attempts. Try again later.");
authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identity,request.password()));
User user=users.findByUsernameIgnoreCaseOrEmailIgnoreCase(identity,identity).orElseThrow();
String access=jwtService.createAccessToken(user);
String refresh=refreshTokens.issue(user);
return new AuthResponse(access,refresh,jwtService.getAccessExpirationSeconds(),user.getId(),user.getUsername(),user.getRoles().stream().map(Enum::name).sorted().toList());
}

@Transactional
public AuthResponse refresh(RefreshRequest request){
User user=refreshTokens.validateAndRevoke(request.refreshToken());
String access=jwtService.createAccessToken(user);
String refresh=refreshTokens.issue(user);
return new AuthResponse(access,refresh,jwtService.getAccessExpirationSeconds(),user.getId(),user.getUsername(),user.getRoles().stream().map(Enum::name).sorted().toList());
}

@Transactional
public void logout(LogoutRequest request){refreshTokens.revoke(request.refreshToken());}

public UserDetails load(String username){return new org.springframework.security.core.userdetails.User(username,"",java.util.List.of());}
}
