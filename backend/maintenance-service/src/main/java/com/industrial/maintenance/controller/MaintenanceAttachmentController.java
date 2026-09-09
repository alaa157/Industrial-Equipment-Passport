package com.industrial.maintenance.controller;

import com.industrial.maintenance.entity.MaintenanceAttachment;
import com.industrial.maintenance.repository.MaintenanceAttachmentRepository;
import com.industrial.maintenance.service.LocalFileStorage;
import java.nio.file.*;
import java.util.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/maintenance/{maintenanceId}/attachments")
public class MaintenanceAttachmentController {
private final MaintenanceAttachmentRepository repository;
private final LocalFileStorage storage;

public MaintenanceAttachmentController(MaintenanceAttachmentRepository repository,LocalFileStorage storage){
this.repository=repository;this.storage=storage;
}

@PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> upload(@PathVariable UUID maintenanceId,@RequestPart("file") MultipartFile file)throws Exception{
LocalFileStorage.Stored stored=storage.store(file);
return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(
new MaintenanceAttachment(maintenanceId,stored.originalFilename(),stored.storedFilename(),stored.contentType(),stored.sizeBytes(),stored.relativePath())
));
}

@GetMapping
public List<MaintenanceAttachment> list(@PathVariable UUID maintenanceId){
return repository.findByMaintenanceIdOrderByCreatedAtDesc(maintenanceId);
}

@GetMapping("/{attachmentId}/download")
public ResponseEntity<Resource> download(@PathVariable UUID maintenanceId,@PathVariable UUID attachmentId){
MaintenanceAttachment a=repository.findById(attachmentId).orElseThrow(()->new NoSuchElementException("Attachment not found"));
if(!a.getMaintenanceId().equals(maintenanceId))throw new NoSuchElementException("Attachment not found");
Resource resource=new FileSystemResource(storage.load(a.getRelativePath()));
return ResponseEntity.ok().contentType(MediaType.parseMediaType(a.getContentType())).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+a.getOriginalFilename().replace("\"","")+"\"").body(resource);
}
}
