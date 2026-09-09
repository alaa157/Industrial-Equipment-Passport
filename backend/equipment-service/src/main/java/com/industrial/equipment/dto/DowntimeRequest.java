package com.industrial.equipment.dto;

import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.UUID;

public record DowntimeRequest(
@NotBlank @Size(max=500) String reason,
@NotNull Instant startTime,
Instant endTime,
UUID relatedMaintenanceId,
@Size(max=1000) String notes
){}
