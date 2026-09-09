package com.industrial.maintenance.repository;

import com.industrial.maintenance.entity.Technician;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicianRepository extends JpaRepository<Technician,UUID>{
List<Technician> findByActiveTrueOrderByName();
}
