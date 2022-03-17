package com.demo.common.cors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "blade.cors", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(CorsProperties.class)
public class CorsConfig {

    @Bean
    public CorsFilter getCorsFilter(CorsProperties corsProperties) {
        return new CorsFilter(corsProperties);
    }
}
