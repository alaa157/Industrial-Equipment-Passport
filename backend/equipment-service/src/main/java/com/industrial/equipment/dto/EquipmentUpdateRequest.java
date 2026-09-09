package com.industrial.equipment.dto;

import com.industrial.equipment.entity.Criticality;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EquipmentUpdateRequest(
@NotBlank @Size(max=180) String name,
@Size(max=1000) String description,
@NotBlank @Size(max=150) String manufacturer,
@NotBlank @Size(max=150) String model,
LocalDate warrantyExpiration,
@NotNull Criticality criticality,
@Size(max=120) String responsibleDepartment,
@Size(max=2000) String notes
){}
