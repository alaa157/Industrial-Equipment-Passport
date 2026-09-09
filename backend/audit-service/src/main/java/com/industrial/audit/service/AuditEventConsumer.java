package com.industrial.audit.service;

import com.industrial.audit.entity.AuditLog;
import com.industrial.audit.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class AuditEventConsumer {
    private final AuditLogRepository repository;
    private final ObjectMapper mapper;

    public AuditEventConsumer(AuditLogRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "audit.events", durable = "true"), exchange = @Exchange(value = "iep.domain.events", type = "topic", durable = "true"), key = {
            "#" }))
    public void consume(String message) {
        try {
            JsonNode root = mapper.readTree(message);
            String type = root.path("type").asText("DOMAIN_EVENT");
            JsonNode payload = root.path("payload");
            UUID entityId = null;
            String entityType = type;
            if (payload.hasNonNull("equipmentId"))
                entityId = UUID.fromString(payload.get("equipmentId").asText());
            else if (payload.hasNonNull("maintenanceId"))
                entityId = UUID.fromString(payload.get("maintenanceId").asText());
            repository.save(new AuditLog(null, type, entityType, entityId, null, null, null, payload.toString()));
        } catch (Exception ex) {
            throw new IllegalStateException("Audit event could not be processed", ex);
        }
    }
}
