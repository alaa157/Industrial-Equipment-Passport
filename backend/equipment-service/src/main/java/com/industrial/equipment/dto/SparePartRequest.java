package com.industrial.equipment.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SparePartRequest(
@NotBlank @Size(max=80) String partNumber,
@NotBlank @Size(max=180) String name,
@Size(max=1000) String description,
@Size(max=150) String manufacturer,
@Min(0) int quantity,
@Min(0) int minimumQuantity,
@NotNull @DecimalMin("0.00") BigDecimal unitCost,
@Size(max=150) String location
){}
