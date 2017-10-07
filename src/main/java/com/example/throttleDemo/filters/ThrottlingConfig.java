package com.example.throttleDemo.filters;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.Filter;

@Configuration
public class ThrottlingConfig {
    public static final long MAX_REQ_LIMIT = 6;  // default per hour


    @Bean
    public Filter throttlingFilter() {
        return new ThrottlingFilter();
    }

    @Bean
    StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
