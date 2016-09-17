package ru.itchannel.ycsearcher;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.itchannel.ycsearcher.concurrent.Searcher;
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.Channel;
import ru.itchannel.ycsearcher.service.PageService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication
@EnableAsync
public class YoutubeChannelSearcherApplication extends AsyncConfigurerSupport {
    public static final String CHANNELS_MAP = "CHANNELS_MAP";
    public static final String VISITED_URLS_SET = "VISITED_URLS_SET";
    public static final String THREAD_NAME_PREFIX = "YoutubeChannelSearcher-";
    private static final Logger log = LoggerFactory.getLogger(YoutubeChannelSearcherApplication.class);
    @Value("${app.first.page.url}")
    private String firstPageUrl;
    @Value("${app.parallel.searchers.quantity}")
    private int parallelSearchersQuantity;
    @Autowired
    private PageService pageService;
    @Autowired
    private ChannelPool channelPool;
    @Autowired
    private ThreadPoolTaskExecutor executor;

    public static void main(String[] args) {
        SpringApplication.run(YoutubeChannelSearcherApplication.class, args);
    }

    @Override
    public Executor getAsyncExecutor() {
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(parallelSearchersQuantity);
        executor.setMaxPoolSize(parallelSearchersQuantity);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance();
    }

    @Bean
    public BlockingQueue<String> processingQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public Map<String, Channel> channelPool(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap(CHANNELS_MAP);
    }

    @Bean
    public Map<String, Object> visitedUrlsMap(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap(VISITED_URLS_SET);
    }

    @Bean
    public CommandLineRunner run(BlockingQueue<String> processingQueue, ApplicationContext context) {
        return args -> {
            log.info("All: " + channelPool.findAll().toString());
            Set<String> nextUrls = pageService.processVideoPage(firstPageUrl);
            processingQueue.addAll(nextUrls);

            for (int i = parallelSearchersQuantity; i > 0; i--) {
                Searcher searcher = (Searcher) context.getBean("searcher");
                searcher.start();
            }
        };
    }
}
