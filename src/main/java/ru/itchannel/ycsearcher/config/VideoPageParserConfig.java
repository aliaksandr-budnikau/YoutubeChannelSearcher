package ru.itchannel.ycsearcher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class VideoPageParserConfig {

    public static final String VIDEO_PAGE_PARSER_THREAD_NAME_PREFIX = "YoutubeChannelVideoPageParser-";

    @Value("${app.parallel.page.parser.quantity}")
    private int parallelVideoPageParserQuantity;

    @Bean
    public ThreadPoolTaskExecutor asyncVideoPageParserExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(parallelVideoPageParserQuantity);
        executor.setMaxPoolSize(parallelVideoPageParserQuantity);
        executor.setThreadNamePrefix(VIDEO_PAGE_PARSER_THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
}
