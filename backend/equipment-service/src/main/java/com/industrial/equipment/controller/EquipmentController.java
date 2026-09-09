package com.industrial.equipment.controller;

import com.industrial.equipment.dto.*;
import com.industrial.equipment.entity.*;
import com.industrial.equipment.service.*;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/equipment")
public class EquipmentController {

    private final EquipmentService service;
    private final InspectionService inspections;
    private final SparePartService parts;
    private final DowntimeService downtime;
    private final FaultService faults;
    private final QrCodeService qr;

    public EquipmentController(
            EquipmentService service,
            InspectionService inspections,
            SparePartService parts,
            DowntimeService downtime,
            FaultService faults,
            QrCodeService qr) {
        this.service = service;
        this.inspections = inspections;
        this.parts = parts;
        this.downtime = downtime;
        this.faults = faults;
        this.qr = qr;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EquipmentResponse> create(
            @Valid @RequestBody EquipmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @GetMapping
    public Page<EquipmentResponse> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) EquipmentStatus status,
            @RequestParam(required = false) Criticality criticality,
            @RequestParam(required = false) UUID siteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        return service.search(
                query, status, criticality, siteId, page, size, sort, direction);
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return service.dashboard();
    }

    @GetMapping("/{id}")
    public EquipmentResponse get(@PathVariable UUID id) {
        return EquipmentResponse.from(service.get(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN')")
    public EquipmentResponse status(
            @PathVariable UUID id,
            @Valid @RequestBody StatusChangeRequest request) {
        return service.changeStatus(id, request.status());
    }

    @GetMapping("/{id}/qr")
    public ResponseEntity<byte[]> qr(@PathVariable UUID id) {
        EquipmentResponse equipment =
                EquipmentResponse.from(service.get(id));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=" + equipment.assetCode() + "-qr.png")
                .body(qr.generate("iep://equipment/" + equipment.id()));
    }

    @PostMapping("/{id}/inspections")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','INSPECTOR')")
    public ResponseEntity<?> inspection(
            @PathVariable UUID id,
            @Valid @RequestBody InspectionRequest request) {

        var authentication =
                org.springframework.security.core.context.SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        UUID inspector = UUID.fromString(
                ((org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken)
                        authentication)
                        .getToken()
                        .getSubject());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inspections.create(id, inspector, request));
    }

    @GetMapping("/{id}/inspections")
    public List<Inspection> inspections(@PathVariable UUID id) {
        return inspections.findByEquipment(id);
    }

    @PostMapping("/{id}/downtime")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<?> downtime(
            @PathVariable UUID id,
            @Valid @RequestBody DowntimeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(downtime.create(id, request));
    }

    @GetMapping("/{id}/downtime")
    public List<DowntimeRecord> downtime(@PathVariable UUID id) {
        return downtime.findByEquipment(id);
    }

    @PostMapping("/{id}/faults")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','TECHNICIAN','INSPECTOR')")
    public ResponseEntity<?> fault(
            @PathVariable UUID id,
            @Valid @RequestBody FaultRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(faults.create(id, request));
    }

    @GetMapping("/{id}/faults")
    public List<FaultReport> faults(@PathVariable UUID id) {
        return faults.findByEquipment(id);
    }
}