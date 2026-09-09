package com.industrial.equipment.repository;

import com.industrial.equipment.entity.*;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionRepository extends JpaRepository<Inspection,UUID>{
List<Inspection> findByEquipmentIdOrderByInspectionDateDesc(UUID equipmentId);
long countByResult(InspectionResult result);
}
