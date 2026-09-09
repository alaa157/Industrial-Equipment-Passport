package com.industrial.equipment.dto;

import com.industrial.equipment.entity.Criticality;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public record EquipmentRequest(
@NotBlank @Size(max=60) String assetCode,
@NotBlank @Size(max=120) String serialNumber,
@NotBlank @Size(max=180) String name,
@Size(max=1000) String description,
@NotBlank @Size(max=150) String manufacturer,
@NotBlank @Size(max=150) String model,
@NotNull UUID equipmentTypeId,
LocalDate purchaseDate,
LocalDate installationDate,
LocalDate warrantyExpiration,
@NotNull Criticality criticality,
UUID siteId,
UUID buildingId,
UUID areaId,
@Size(max=120) String responsibleDepartment,
UUID responsibleTechnicianId,
@Size(max=2000) String notes
){}
