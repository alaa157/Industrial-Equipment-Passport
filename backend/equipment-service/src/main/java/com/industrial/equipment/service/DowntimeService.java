package com.industrial.equipment.service;

import com.industrial.equipment.dto.DowntimeRequest;
import com.industrial.equipment.entity.DowntimeRecord;
import com.industrial.equipment.repository.DowntimeRepository;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DowntimeService {
private final DowntimeRepository downtime;
private final EquipmentService equipment;
public DowntimeService(DowntimeRepository downtime,EquipmentService equipment){this.downtime=downtime;this.equipment=equipment;}

@Transactional
public DowntimeRecord create(UUID equipmentId,DowntimeRequest request){
return downtime.save(new DowntimeRecord(equipment.get(equipmentId),request.reason(),request.startTime(),request.endTime(),request.relatedMaintenanceId(),request.notes()));
}

@Transactional(readOnly=true)
public List<DowntimeRecord> findByEquipment(UUID equipmentId){return downtime.findByEquipmentIdOrderByStartTimeDesc(equipmentId);}

@Transactional(readOnly=true)
public long totalMinutes(){
return downtime.findAll().stream().mapToLong(DowntimeRecord::getDurationMinutes).sum();
}
}
