package com.industrial.audit.repository;

import com.industrial.audit.entity.AuditLog;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog,UUID>{
Page<AuditLog> findAllByOrderByTimestampDesc(Pageable pageable);
}
