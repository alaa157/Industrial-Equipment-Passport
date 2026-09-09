package com.industrial.equipment.repository;

import com.industrial.equipment.entity.DowntimeRecord;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DowntimeRepository extends JpaRepository<DowntimeRecord,UUID>{
List<DowntimeRecord> findByEquipmentIdOrderByStartTimeDesc(UUID equipmentId);
}
