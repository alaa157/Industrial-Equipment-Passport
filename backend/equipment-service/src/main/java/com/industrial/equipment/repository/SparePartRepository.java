package com.industrial.equipment.repository;

import com.industrial.equipment.entity.SparePart;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SparePartRepository extends JpaRepository<SparePart,UUID>{
List<SparePart> findByQuantityLessThanEqual(int quantity);
}
