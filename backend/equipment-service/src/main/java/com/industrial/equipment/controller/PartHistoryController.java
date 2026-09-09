package com.industrial.equipment.controller;

import com.industrial.equipment.dto.SparePartRequest;
import com.industrial.equipment.service.SparePartService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/equipment/parts")
public class PartHistoryController {
private final SparePartService service;

public PartHistoryController(SparePartService service){this.service=service;}

@GetMapping("/{partId}/equipment/{equipmentId}")
public Object installed(@PathVariable UUID partId,@PathVariable UUID equipmentId){
return service.equipmentParts(equipmentId).stream().filter(x->x.getPart().getId().equals(partId)).toList();
}

@GetMapping("/equipment/{equipmentId}/history")
public Object history(@PathVariable UUID equipmentId){return service.replacements(equipmentId);}
}
