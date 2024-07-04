package com.workshop.users.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryConfig {
    @Value("${retryable.max-attempts}")
    private Integer maxAttempts;
    @Value("${retryable.backoff}")
    private Integer backoff;

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public long getBackoff() {
        return backoff;
    }

}
