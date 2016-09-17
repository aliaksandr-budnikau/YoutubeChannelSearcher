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
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.Channel;
import ru.itchannel.ycsearcher.service.PageService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication
public class YoutubeChannelSearcherApplication {
    public static final String CHANNELS_MAP = "CHANNELS_MAP";
    public static final String VISITED_URLS_SET = "VISITED_URLS_SET";
    private static final Logger log = LoggerFactory.getLogger(YoutubeChannelSearcherApplication.class);
    @Value("${app.url.youtube.host}")
    private String youtubeHost;
    @Value("${app.first.page.url}")
    private String firstPageUrl;
    @Value("${app.parallel.searchers.quantity}")
    private int parallelSearchersQuantity;
    @Value("${app.max.processing.queue.size}")
    private int maxPocessingQueueuSize;
    @Autowired
    private PageService pageService;
    @Autowired
    private ChannelPool channelPool;

    public static void main(String[] args) {
        SpringApplication.run(YoutubeChannelSearcherApplication.class, args);
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance();
    }

    @Bean
    public ExecutorService executorService(ApplicationContext context) {
        ExecutorService executorService = Executors.newFixedThreadPool(parallelSearchersQuantity);
        for (int i = parallelSearchersQuantity; i > 0; i--) {
            executorService.execute((Runnable) context.getBean("searcher"));
        }
        return executorService;
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
    public CommandLineRunner run(BlockingQueue<String> processingQueue) {
        return args -> {
            log.info("All: " + channelPool.findAll().toString());
            Set<String> nextUrls = pageService.processVideoPage(firstPageUrl);
            processingQueue.addAll(nextUrls);
        };
    }
}
