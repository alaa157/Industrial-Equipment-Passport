package com.industrial.equipment.controller;

import com.industrial.equipment.entity.*;
import com.industrial.equipment.repository.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/equipment/catalog")
public class CatalogController {
private final EquipmentTypeRepository types;
private final SiteRepository sites;
private final BuildingRepository buildings;
private final AreaRepository areas;

public CatalogController(EquipmentTypeRepository types,SiteRepository sites,BuildingRepository buildings,AreaRepository areas){
this.types=types;this.sites=sites;this.buildings=buildings;this.areas=areas;
}

@GetMapping("/types")
public List<EquipmentType> types(){return types.findAll(org.springframework.data.domain.Sort.by("name"));}

@GetMapping("/sites")
public List<Site> sites(){return sites.findAll(org.springframework.data.domain.Sort.by("name"));}

@GetMapping("/buildings")
public List<Building> buildings(@RequestParam UUID siteId){
return buildings.findAll().stream().filter(x->x.getSite().getId().equals(siteId)).toList();
}

@GetMapping("/areas")
public List<Area> areas(@RequestParam UUID buildingId){
return areas.findAll().stream().filter(x->x.getBuilding().getId().equals(buildingId)).toList();
}
}
