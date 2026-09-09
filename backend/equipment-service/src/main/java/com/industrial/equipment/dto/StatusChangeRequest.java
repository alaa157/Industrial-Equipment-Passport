package com.industrial.equipment.dto;

import com.industrial.equipment.entity.EquipmentStatus;
import jakarta.validation.constraints.NotNull;

public record StatusChangeRequest(@NotNull EquipmentStatus status){}
