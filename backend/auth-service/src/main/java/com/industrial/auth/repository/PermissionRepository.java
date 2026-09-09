package com.industrial.auth.repository;

import com.industrial.auth.entity.Permission;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,UUID>{
Optional<Permission> findByCode(String code);
}
