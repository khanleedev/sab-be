package org.project.social_account_business.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        val cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of("otpAttempts", "account", "transaction", "paymentTransaction", "order", "currency"));
        cacheManager.setCaffeine(caffeineConfig());
        return cacheManager;
    }

    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100)
                .recordStats();
    }
}