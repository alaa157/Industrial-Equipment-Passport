package com.industrial.maintenance.repository;

import com.industrial.maintenance.entity.MaintenanceAttachment;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceAttachmentRepository extends JpaRepository<MaintenanceAttachment,UUID>{
List<MaintenanceAttachment> findByMaintenanceIdOrderByCreatedAtDesc(UUID maintenanceId);
}
