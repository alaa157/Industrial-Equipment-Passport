package com.industrial.equipment.controller;

import com.industrial.equipment.entity.Attachment;
import com.industrial.equipment.repository.*;
import com.industrial.equipment.service.FileStorageService;
import java.nio.file.*;
import java.util.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/equipment/{equipmentId}/attachments")
public class AttachmentController {
    private final AttachmentRepository attachments;
    private final EquipmentRepository equipment;
    private final FileStorageService storage;

    public AttachmentController(AttachmentRepository attachments, EquipmentRepository equipment,
            FileStorageService storage) {
        this.attachments = attachments;
        this.equipment = equipment;
        this.storage = storage;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@PathVariable UUID equipmentId, @RequestPart("file") MultipartFile file) {
        var asset = equipment.findById(equipmentId)
                .orElseThrow(() -> new NoSuchElementException("Equipment not found"));
        var stored = storage.store(file, "equipment");
        Attachment saved = attachments.save(new Attachment(asset, stored.originalFilename(), stored.storedFilename(),
                stored.contentType(), stored.sizeBytes(), stored.relativePath()));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Attachment> list(@PathVariable UUID equipmentId) {
        return attachments.findByEquipmentIdOrderByCreatedAtDesc(equipmentId);
    }

    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> download(
            @PathVariable UUID equipmentId,
            @PathVariable UUID attachmentId) {

        Attachment attachment = attachments.findById(attachmentId)
                .orElseThrow(() -> new NoSuchElementException("Attachment not found"));

        if (!attachment.getEquipment().getId().equals(equipmentId)) {
            throw new NoSuchElementException("Attachment not found");
        }

        Path path = storage.load(attachment.getRelativePath());
        Resource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            throw new NoSuchElementException("Stored file is missing");
        }

        String safeName = attachment.getOriginalFilename()
                .replace("\"", "");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        attachment.getContentType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + safeName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> delete(@PathVariable UUID equipmentId, @PathVariable UUID attachmentId) {
        Attachment attachment = attachments.findById(attachmentId)
                .orElseThrow(() -> new NoSuchElementException("Attachment not found"));
        if (!attachment.getEquipment().getId().equals(equipmentId))
            throw new NoSuchElementException("Attachment not found");
        storage.delete(attachment.getRelativePath());
        attachments.delete(attachment);
        return ResponseEntity.noContent().build();
    }
}
