package com.industrial.auth.dto;

import com.industrial.auth.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record UpdateUserRolesRequest(@NotEmpty Set<Role> roles){}
