package com.industrial.equipment.repository;

import com.industrial.equipment.entity.FaultReport;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaultReportRepository extends JpaRepository<FaultReport,UUID>{
List<FaultReport> findByEquipmentIdOrderByReportedAtDesc(UUID equipmentId);
}
