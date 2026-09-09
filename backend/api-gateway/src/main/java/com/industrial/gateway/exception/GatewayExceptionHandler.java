package com.industrial.gateway.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GatewayExceptionHandler {
@ExceptionHandler(Exception.class)
public ResponseEntity<Map<String,Object>> handle(Exception ex) {
Map<String,Object> body=new LinkedHashMap<>();
body.put("timestamp",Instant.now().toString());
body.put("status",HttpStatus.INTERNAL_SERVER_ERROR.value());
body.put("error","GATEWAY_ERROR");
body.put("message","The request could not be processed.");
body.put("correlationId",UUID.randomUUID().toString());
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
}
}
