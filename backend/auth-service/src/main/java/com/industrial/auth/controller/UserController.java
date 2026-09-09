package com.industrial.auth.controller;

import com.industrial.auth.dto.UserResponse;
import com.industrial.auth.repository.UserRepository;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
private final UserRepository users;
public UserController(UserRepository users){this.users=users;}

@GetMapping("/me")
public ResponseEntity<UserResponse> currentUser(Principal principal){
return users.findByUsernameIgnoreCaseOrEmailIgnoreCase(principal.getName(),principal.getName())
.map(UserResponse::from).map(ResponseEntity::ok)
.orElseGet(()->ResponseEntity.notFound().build());
}

@GetMapping
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> allUsers(){return ResponseEntity.ok(users.findAll().stream().map(UserResponse::from).toList());}
}
