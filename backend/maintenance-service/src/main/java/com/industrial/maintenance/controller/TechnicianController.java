package com.industrial.maintenance.controller;

import com.industrial.maintenance.entity.Technician;
import com.industrial.maintenance.repository.TechnicianRepository;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/technicians")
public class TechnicianController {
private final TechnicianRepository repository;

public TechnicianController(TechnicianRepository repository){this.repository=repository;}

@GetMapping
public List<Technician> list(){return repository.findByActiveTrueOrderByName();}

@PostMapping
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public Technician create(@RequestBody Technician technician){return repository.save(technician);}
}
