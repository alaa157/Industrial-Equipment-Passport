package com.industrial.equipment.controller;

import com.industrial.equipment.dto.*;
import com.industrial.equipment.repository.EquipmentRepository;
import com.industrial.equipment.service.EquipmentService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/equipment")
public class EquipmentEditController {
private final EquipmentRepository repository;
private final EquipmentService service;

public EquipmentEditController(EquipmentRepository repository,EquipmentService service){
this.repository=repository;this.service=service;
}

@PatchMapping("/{id}")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public EquipmentResponse update(@PathVariable UUID id,@Valid @RequestBody EquipmentUpdateRequest request){
service.get(id);
if(repository.updateEditableFields(id,request.name(),request.description(),request.manufacturer(),request.model(),request.warrantyExpiration(),request.criticality(),request.responsibleDepartment(),request.notes())==0)
throw new NoSuchElementException("Equipment not found");
return EquipmentResponse.from(service.get(id));
}
}
