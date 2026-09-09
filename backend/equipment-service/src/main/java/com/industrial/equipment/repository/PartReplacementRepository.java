package com.industrial.equipment.repository;

import com.industrial.equipment.entity.PartReplacement;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartReplacementRepository extends JpaRepository<PartReplacement,UUID>{
List<PartReplacement> findByEquipmentIdOrderByReplacedAtDesc(UUID equipmentId);
}
