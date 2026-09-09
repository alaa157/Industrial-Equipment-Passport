package com.industrial.auth.dto;

import com.industrial.auth.entity.User;
import java.util.*;

public record UserResponse(
UUID id,
String username,
String email,
List<String> roles,
boolean enabled
){
public static UserResponse from(User u){
return new UserResponse(
u.getId(),
u.getUsername(),
u.getEmail(),
u.getRoles().stream().map(Enum::name).sorted().toList(),
u.isEnabled()
);
}
}
