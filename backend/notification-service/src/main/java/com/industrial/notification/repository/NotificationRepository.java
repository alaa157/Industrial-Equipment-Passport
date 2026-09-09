package com.industrial.notification.repository;

import com.industrial.notification.entity.Notification;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,UUID>{
List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);
long countByUserIdAndReadAtIsNull(UUID userId);
}
