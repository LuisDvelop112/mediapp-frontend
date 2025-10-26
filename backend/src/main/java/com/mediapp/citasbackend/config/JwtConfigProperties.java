package com.mediapp.citasbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfigProperties {
    private String secret;
    private long expiration;
    private long refreshExpiration;
}
