package com.industrial.auth.dto;

import com.industrial.auth.entity.Role;
import jakarta.validation.constraints.*;
import java.util.Set;

public record CreateUserRequest(
@NotBlank @Size(min=3,max=80) String username,
@NotBlank @Email @Size(max=180) String email,
@NotBlank @Size(min=10,max=100) String password,
@NotEmpty Set<Role> roles
){}
