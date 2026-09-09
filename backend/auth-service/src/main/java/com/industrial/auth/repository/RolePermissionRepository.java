package com.industrial.auth.repository;

import com.industrial.auth.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission,UUID>{
List<RolePermission> findByRoleIn(Collection<Role> roles);
List<RolePermission> findByRole(Role role);
}
