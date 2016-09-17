package ru.itchannel.ycsearcher.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
public class ConcurrentConfig extends AsyncConfigurerSupport {

    public static final String SEARCHER_THREAD_NAME_PREFIX = "YoutubeChannelSearcher-";

    @Value("${app.parallel.searchers.quantity}")
    private int parallelSearchersQuantity;
    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Bean
    public ThreadPoolTaskExecutor asyncSearchExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(parallelSearchersQuantity);
        executor.setMaxPoolSize(parallelSearchersQuantity);
        executor.setThreadNamePrefix(SEARCHER_THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return executor;
    }
}
