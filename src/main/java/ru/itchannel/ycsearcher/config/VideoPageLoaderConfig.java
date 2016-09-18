package ru.itchannel.ycsearcher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class VideoPageLoaderConfig {

    public static final String VIDEO_PAGE_LOADER_THREAD_NAME_PREFIX = "YoutubeChannelVideoPageLoader-";

    @Value("${app.parallel.page.loader.quantity}")
    private int parallelVideoPageLoaderQuantity;

    @Bean
    public ThreadPoolTaskExecutor asyncVideoPageLoaderExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(parallelVideoPageLoaderQuantity);
        executor.setMaxPoolSize(parallelVideoPageLoaderQuantity);
        executor.setThreadNamePrefix(VIDEO_PAGE_LOADER_THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
}
