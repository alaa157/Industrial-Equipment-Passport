package com.industrial.auth.service;

import com.industrial.auth.dto.*;
import com.industrial.auth.entity.*;
import com.industrial.auth.repository.*;
import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAdminService {
private final UserRepository users;
private final PasswordEncoder encoder;
private final PermissionRepository permissions;
private final RolePermissionRepository rolePermissions;

public UserAdminService(UserRepository users,PasswordEncoder encoder,PermissionRepository permissions,RolePermissionRepository rolePermissions){
this.users=users;
this.encoder=encoder;
this.permissions=permissions;
this.rolePermissions=rolePermissions;
}

@Transactional
public UserResponse create(CreateUserRequest request){
if(users.existsByUsernameIgnoreCase(request.username()))throw new IllegalArgumentException("Username already exists");
if(users.existsByEmailIgnoreCase(request.email()))throw new IllegalArgumentException("Email already exists");
User user=users.save(new User(request.username().trim(),request.email().trim().toLowerCase(),encoder.encode(request.password()),request.roles()));
return UserResponse.from(user);
}

@Transactional
public UserResponse updateRoles(UUID id,UpdateUserRolesRequest request){
User user=users.findById(id).orElseThrow(()->new NoSuchElementException("User not found"));
user.setRoles(request.roles());
return UserResponse.from(users.save(user));
}

@Transactional
public void setEnabled(UUID id,boolean enabled){
User user=users.findById(id).orElseThrow(()->new NoSuchElementException("User not found"));
user.setEnabled(enabled);
users.save(user);
}

@Transactional(readOnly=true)
public Map<String,List<String>> rolePermissions(){
Map<String,List<String>> result=new LinkedHashMap<>();
for(Role role:Role.values()){
result.put(role.name(),rolePermissions.findByRole(role).stream().map(x->x.getPermission().getCode()).sorted().toList());
}
return result;
}
}
