package com.industrial.equipment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
private final RabbitTemplate rabbit;
private final ObjectMapper mapper;

public EventPublisher(RabbitTemplate rabbit,ObjectMapper mapper){
this.rabbit=rabbit;this.mapper=mapper;
}

public void publish(String type,Object payload){
try{
Map<String,Object> event=new LinkedHashMap<>();
event.put("type",type);
event.put("timestamp",Instant.now().toString());
event.put("payload",payload);
rabbit.convertAndSend("iep.domain.events",type,mapper.writeValueAsString(event));
}catch(Exception ex){
throw new IllegalStateException("Unable to publish domain event",ex);
}
}
}
