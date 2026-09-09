package com.industrial.audit.controller;

import com.industrial.audit.repository.AuditLogRepository;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER','VIEWER')")
public class AuditController {
private final AuditLogRepository repository;

public AuditController(AuditLogRepository repository){this.repository=repository;}

@GetMapping
public Page<?> all(
@RequestParam(defaultValue="0") int page,
@RequestParam(defaultValue="50") int size){
return repository.findAllByOrderByTimestampDesc(PageRequest.of(Math.max(0,page),Math.min(Math.max(1,size),200)));
}
}
