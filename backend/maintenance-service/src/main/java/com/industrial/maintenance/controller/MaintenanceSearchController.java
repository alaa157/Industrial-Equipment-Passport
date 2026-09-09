package com.industrial.maintenance.controller;

import com.industrial.maintenance.dto.MaintenanceResponse;
import com.industrial.maintenance.entity.*;
import com.industrial.maintenance.repository.MaintenanceSearchRepository;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/maintenance/search")
public class MaintenanceSearchController {
private final MaintenanceSearchRepository repository;

public MaintenanceSearchController(MaintenanceSearchRepository repository){this.repository=repository;}

@GetMapping
public org.springframework.data.domain.Page<MaintenanceResponse> search(
@RequestParam(required=false) String query,
@RequestParam(required=false) MaintenanceStatus status,
@RequestParam(required=false) MaintenancePriority priority,
@RequestParam(required=false) UUID technicianId,
@RequestParam(defaultValue="0") int page,
@RequestParam(defaultValue="20") int size){
return repository.search(query,status,priority,technicianId,page,size).map(MaintenanceResponse::from);
}
}
