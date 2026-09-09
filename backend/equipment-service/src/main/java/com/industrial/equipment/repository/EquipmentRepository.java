package com.industrial.equipment.repository;

import com.industrial.equipment.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface EquipmentRepository extends JpaRepository<Equipment,UUID>,JpaSpecificationExecutor<Equipment>{
boolean existsByAssetCodeIgnoreCase(String assetCode);
boolean existsBySerialNumberIgnoreCase(String serialNumber);
long countByStatus(EquipmentStatus status);

@Modifying
@Query("""
update Equipment e set
e.name=:name,
e.description=:description,
e.manufacturer=:manufacturer,
e.model=:model,
e.warrantyExpiration=:warrantyExpiration,
e.criticality=:criticality,
e.responsibleDepartment=:department,
e.notes=:notes,
e.updatedAt=CURRENT_TIMESTAMP
where e.id=:id
""")
int updateEditableFields(
@Param("id") UUID id,
@Param("name") String name,
@Param("description") String description,
@Param("manufacturer") String manufacturer,
@Param("model") String model,
@Param("warrantyExpiration") java.time.LocalDate warrantyExpiration,
@Param("criticality") Criticality criticality,
@Param("department") String department,
@Param("notes") String notes
);
}
