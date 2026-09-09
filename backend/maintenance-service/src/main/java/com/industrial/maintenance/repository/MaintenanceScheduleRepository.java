package com.industrial.maintenance.repository;

import com.industrial.maintenance.entity.MaintenanceSchedule;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule,UUID>{
List<MaintenanceSchedule> findByActiveTrueAndNextRunDateLessThanEqual(LocalDate date);
}
