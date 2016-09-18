package ru.itchannel.ycsearcher;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import ru.itchannel.ycsearcher.concurrent.VideoPageLoader;
import ru.itchannel.ycsearcher.concurrent.VideoPageParser;
import ru.itchannel.ycsearcher.dao.parser.Document;
import ru.itchannel.ycsearcher.distribute.UrlsSet;
import ru.itchannel.ycsearcher.distribute.impl.UrlsSetImpl;
import ru.itchannel.ycsearcher.dto.Channel;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication
public class YoutubeChannelSearcherApplication {
    public static final String CHANNELS_MAP = "CHANNELS_MAP";
    public static final String LOADED_URLS_SET = "LOADED_URLS_SET";
    public static final String PARSED_URLS_SET = "PARSED_URLS_SET";
    @Value("${app.first.page.url}")
    private String firstPageUrl;
    @Value("${app.parallel.page.loader.quantity}")
    private int parallelPageLoaderQuantity;
    @Value("${app.parallel.page.parser.quantity}")
    private int parallelPageParserQuantity;

    public static void main(String[] args) {
        SpringApplication.run(YoutubeChannelSearcherApplication.class, args);
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance();
    }

    @Bean
    public BlockingQueue<String> urlsQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public BlockingQueue<Document> videoPagesQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public Map<String, Channel> channelPool(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap(CHANNELS_MAP);
    }

    @Bean
    public UrlsSet loadedPagesUrls(HazelcastInstance hazelcastInstance) {
        IMap<String, Object> map = hazelcastInstance.getMap(LOADED_URLS_SET);
        return new UrlsSetImpl(map);
    }

    @Bean
    public UrlsSet parsedPagesUrls(HazelcastInstance hazelcastInstance) {
        IMap<String, Object> map = hazelcastInstance.getMap(PARSED_URLS_SET);
        return new UrlsSetImpl(map);
    }

    @Bean
    public CommandLineRunner run(@Qualifier("urlsQueue") BlockingQueue<String> urlsQueue, ApplicationContext context) {
        return args -> {
            urlsQueue.put(firstPageUrl);

            for (int i = parallelPageLoaderQuantity; i > 0; i--) {
                VideoPageLoader loader = (VideoPageLoader) context.getBean("videoPageLoader");
                loader.start();
            }

            for (int i = parallelPageParserQuantity; i > 0; i--) {
                VideoPageParser parser = (VideoPageParser) context.getBean("videoPageParser");
                parser.start();
            }
        };
    }
}
