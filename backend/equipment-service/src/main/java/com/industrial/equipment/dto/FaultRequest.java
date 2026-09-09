package com.industrial.equipment.dto;

import com.industrial.equipment.entity.FaultSeverity;
import jakarta.validation.constraints.*;

public record FaultRequest(
@NotBlank @Size(max=180) String title,
@NotBlank @Size(max=3000) String description,
@NotNull FaultSeverity severity
){}
