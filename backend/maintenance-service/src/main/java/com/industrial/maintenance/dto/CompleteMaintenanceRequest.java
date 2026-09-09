package com.industrial.maintenance.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CompleteMaintenanceRequest(
@NotNull Integer actualDurationMinutes,
@NotNull BigDecimal maintenanceCost,
String laborNotes,
String completionNotes
){}
