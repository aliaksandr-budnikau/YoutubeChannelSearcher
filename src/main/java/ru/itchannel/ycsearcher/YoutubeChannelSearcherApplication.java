package ru.itchannel.ycsearcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import ru.itchannel.ycsearcher.concurrent.Searcher;
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.service.PageService;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication
public class YoutubeChannelSearcherApplication {
    private static final Logger log = LoggerFactory.getLogger(YoutubeChannelSearcherApplication.class);
    @Value("${app.first.page.url}")
    private String firstPageUrl;
    @Value("${app.parallel.searchers.quantity}")
    private int parallelSearchersQuantity;
    @Autowired
    private PageService pageService;
    @Autowired
    private ChannelPool channelPool;

    public static void main(String[] args) {
        SpringApplication.run(YoutubeChannelSearcherApplication.class, args);
    }

    @Bean
    public BlockingQueue<String> processingQueue() {
        return new LinkedBlockingQueue<>();
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
