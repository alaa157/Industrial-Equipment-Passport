package com.industrial.equipment.repository;

import com.industrial.equipment.entity.EquipmentPart;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentPartRepository extends JpaRepository<EquipmentPart,UUID>{
Optional<EquipmentPart> findByEquipmentIdAndPartId(UUID equipmentId,UUID partId);
List<EquipmentPart> findByEquipmentId(UUID equipmentId);
}
