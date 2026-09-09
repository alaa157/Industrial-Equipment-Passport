package com.industrial.notification.controller;

import com.industrial.notification.entity.Notification;
import com.industrial.notification.repository.NotificationRepository;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@PreAuthorize("isAuthenticated()")
public class NotificationController {
private final NotificationRepository repository;

public NotificationController(NotificationRepository repository){this.repository=repository;}

private UUID currentUser(){
return UUID.fromString(((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getToken().getSubject());
}

@GetMapping
public List<Notification> list(){
UUID user=currentUser();
return repository.findAll().stream()
.filter(x->x.getUserId()==null||user.equals(x.getUserId()))
.sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
.toList();
}

@GetMapping("/unread-count")
public long unread(){
UUID user=currentUser();
return repository.findAll().stream().filter(x->(x.getUserId()==null||user.equals(x.getUserId()))&&x.getReadAt()==null).count();
}

@PostMapping("/{id}/read")
public Notification read(@PathVariable UUID id){
Notification notification=repository.findById(id).orElseThrow(()->new NoSuchElementException("Notification not found"));
if(notification.getUserId()!=null&&!notification.getUserId().equals(currentUser()))throw new org.springframework.security.access.AccessDeniedException("Notification does not belong to current user");
notification.markRead();
return repository.save(notification);
}
}
