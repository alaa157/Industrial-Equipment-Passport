package com.industrial.equipment.dto;

import com.industrial.equipment.entity.InspectionResult;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record InspectionRequest(
@NotNull LocalDate inspectionDate,
@NotBlank @Size(max=1200) String checklist,
@NotNull InspectionResult result,
@Size(max=2000) String notes,
LocalDate nextInspectionDate
){}
