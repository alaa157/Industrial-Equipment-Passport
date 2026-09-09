package com.industrial.equipment.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
public static final String EXCHANGE="iep.domain.events";

@Bean TopicExchange domainExchange(){return new TopicExchange(EXCHANGE,true,false);}
}
