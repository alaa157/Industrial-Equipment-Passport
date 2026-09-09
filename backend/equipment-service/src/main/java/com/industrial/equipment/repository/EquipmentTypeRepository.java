package com.industrial.equipment.repository;

import com.industrial.equipment.entity.EquipmentType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentTypeRepository extends JpaRepository<EquipmentType,UUID>{
}
