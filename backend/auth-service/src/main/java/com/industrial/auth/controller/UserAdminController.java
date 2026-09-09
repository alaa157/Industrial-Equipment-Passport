package com.industrial.auth.controller;

import com.industrial.auth.dto.*;
import com.industrial.auth.repository.UserRepository;
import com.industrial.auth.service.UserAdminService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {
private final UserAdminService service;
private final UserRepository users;

public UserAdminController(UserAdminService service,UserRepository users){
this.service=service;
this.users=users;
}

@PostMapping("/users")
public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request){
return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
}

@PatchMapping("/users/{id}/roles")
public UserResponse roles(@PathVariable UUID id,@Valid @RequestBody UpdateUserRolesRequest request){
return service.updateRoles(id,request);
}

@PatchMapping("/users/{id}/enabled")
public ResponseEntity<Void> enabled(@PathVariable UUID id,@RequestParam boolean enabled){
service.setEnabled(id,enabled);
return ResponseEntity.noContent().build();
}

@GetMapping("/permissions")
public Map<String,List<String>> permissions(){return service.rolePermissions();}

@GetMapping("/roles")
public List<String> roles(){return Arrays.stream(com.industrial.auth.entity.Role.values()).map(Enum::name).toList();}
}
