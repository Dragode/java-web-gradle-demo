package com.demo.common.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfig {

    @Bean
    public ExceptionFilter exceptionFilter() {
        return new ExceptionFilter();
    }
}
