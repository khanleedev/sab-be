package org.project.social_account_business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
@Configuration
@EnableAsync
public class ThreadConfig {

    @Value("${thread.pool.size:10}")
    private Integer threadPoolSize;

    @Value("${thread.pool.queue.size:100}")
    private Integer threadQueuePoolSize;

    @Bean(name = "taskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSize);
        executor.setMaxPoolSize(threadPoolSize * 2);
        executor.setQueueCapacity(threadQueuePoolSize);
        executor.setThreadNamePrefix("async-email-");
        executor.initialize();
        return executor;
    }
}