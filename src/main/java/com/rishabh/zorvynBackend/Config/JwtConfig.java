package com.rishabh.zorvynBackend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class JwtConfig {
    @Value("${app.jwt.secret:change-me-in-application-properties}")
    private String secret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long expirationMs;
}