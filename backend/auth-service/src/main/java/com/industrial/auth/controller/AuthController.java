package com.industrial.auth.controller;

import com.industrial.auth.dto.*;
import com.industrial.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
private final AuthService service;
public AuthController(AuthService service){this.service=service;}

@PostMapping("/login")
public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){return ResponseEntity.ok(service.login(request));}

@PostMapping("/refresh")
public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request){return ResponseEntity.ok(service.refresh(request));}

@PostMapping("/logout")
public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request){service.logout(request);return ResponseEntity.noContent().build();}
}
