package com.industrial.equipment.repository;

import com.industrial.equipment.entity.Attachment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment,UUID>{
List<Attachment> findByEquipmentIdOrderByCreatedAtDesc(UUID equipmentId);
}
