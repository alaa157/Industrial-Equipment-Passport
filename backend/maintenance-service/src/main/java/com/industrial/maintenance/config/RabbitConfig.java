package com.industrial.maintenance.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
@Bean
TopicExchange domainExchange(){return new TopicExchange("iep.domain.events",true,false);}
}
