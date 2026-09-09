package com.industrial.equipment.controller;

import com.industrial.equipment.dto.SparePartRequest;
import com.industrial.equipment.service.SparePartService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/equipment/parts")
public class SparePartController {
private final SparePartService service;
public SparePartController(SparePartService service){this.service=service;}

@GetMapping
public Object all(){return service.all();}

@GetMapping("/low-stock")
public Object lowStock(){return service.lowStock();}

@PostMapping
@PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN')")
public ResponseEntity<?> create(@Valid @RequestBody SparePartRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.create(request));
}

@PostMapping("/{id}/consume")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN')")
public Object consume(
        @PathVariable UUID id,
        @RequestParam int quantity,
        @RequestParam UUID equipmentId,
        @RequestParam(required = false) UUID maintenanceId) {
    return service.consume(id, quantity, equipmentId, maintenanceId);
}
}
