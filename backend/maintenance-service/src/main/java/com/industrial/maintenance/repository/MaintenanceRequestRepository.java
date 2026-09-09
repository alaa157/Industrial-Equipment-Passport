package com.industrial.maintenance.repository;

import com.industrial.maintenance.entity.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest,UUID>{
List<MaintenanceRequest> findByStatusOrderByCreatedAtDesc(MaintenanceStatus status);
List<MaintenanceRequest> findByAssignedTechnicianIdOrderByCreatedAtDesc(UUID technicianId);
long countByStatus(MaintenanceStatus status);
long countByPriority(MaintenancePriority priority);
long countByDueDateBeforeAndStatusNot(LocalDate date,MaintenanceStatus status);
}
