package com.industrial.auth.service;

import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {
private final StringRedisTemplate redis;
public RateLimitService(StringRedisTemplate redis){this.redis=redis;}

public boolean allowLogin(String identity){
String key="auth:login:"+identity.toLowerCase();
Long attempts=redis.opsForValue().increment(key);
if(attempts!=null&&attempts==1) redis.expire(key,Duration.ofMinutes(1));
return attempts==null||attempts<=10;
}
}
