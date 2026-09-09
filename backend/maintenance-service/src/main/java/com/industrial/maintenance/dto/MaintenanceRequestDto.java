package com.industrial.maintenance.dto;

import com.industrial.maintenance.entity.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public record MaintenanceRequestDto(
@NotNull UUID equipmentId,
@NotBlank @Size(max=180) String title,
@NotBlank @Size(max=3000) String description,
@NotNull MaintenanceType type,
@NotNull MaintenancePriority priority,
LocalDate dueDate
){}
