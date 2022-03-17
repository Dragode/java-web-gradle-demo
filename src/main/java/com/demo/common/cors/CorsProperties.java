package com.demo.common.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "blade.cors")
public class CorsProperties {
    private String allowOrigin;
    private String allowMethods;
    private Boolean allowHeadersAutoEnabled;
    private String allowHeaders;
    private String allowHeadersAdditional;
    private String allowExposeHeaders;
    private String maxAge;
}
