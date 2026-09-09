package com.industrial.maintenance.controller;

import com.industrial.maintenance.dto.*;
import com.industrial.maintenance.entity.MaintenanceStatus;
import com.industrial.maintenance.service.MaintenanceService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/maintenance")
public class MaintenanceController {

    private final MaintenanceService service;

    public MaintenanceController(MaintenanceService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN')")
    public ResponseEntity<MaintenanceResponse> create(
            @Valid @RequestBody MaintenanceRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @GetMapping
    public List<MaintenanceResponse> list(
            @RequestParam(required = false) MaintenanceStatus status,
            @RequestParam(required = false) UUID technicianId) {
        return service.list(status, technicianId);
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return service.dashboard();
    }

    @GetMapping("/{id}")
    public MaintenanceResponse get(@PathVariable UUID id) {
        return MaintenanceResponse.from(service.get(id));
    }

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN')")
    public MaintenanceResponse assign(
            @PathVariable UUID id,
            @Valid @RequestBody AssignTechnicianRequest request) {
        return service.assign(id, request.technicianId());
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN')")
    public MaintenanceResponse start(@PathVariable UUID id) {
        return service.start(id);
    }

    @PostMapping("/{id}/hold")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN')")
    public MaintenanceResponse hold(@PathVariable UUID id) {
        return service.hold(id);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN')")
    public MaintenanceResponse complete(
            @PathVariable UUID id,
            @Valid @RequestBody CompleteMaintenanceRequest request) {
        return service.complete(id, request);
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN')")
    public MaintenanceResponse cancel(@PathVariable UUID id) {
        return service.cancel(id);
    }
}