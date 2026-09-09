package com.industrial.equipment.repository;

import com.industrial.equipment.entity.Area;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area,UUID>{
}
