package com.industrial.equipment.repository;

import com.industrial.equipment.entity.Building;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building,UUID>{
}
