package com.industrial.auth.dto;

import com.industrial.auth.entity.Permission;

public record PermissionResponse(String code,String description){
public static PermissionResponse from(Permission p){
return new PermissionResponse(p.getCode(),p.getDescription());
}
}
