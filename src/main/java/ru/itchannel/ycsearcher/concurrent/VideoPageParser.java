package ru.itchannel.ycsearcher.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.dao.parser.Document;
import ru.itchannel.ycsearcher.distribute.UrlsSet;
import ru.itchannel.ycsearcher.service.PageService;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

@Component
@Scope("prototype")
public class VideoPageParser {

    private static final Logger log = LoggerFactory.getLogger(VideoPageParser.class);
    @Autowired
    @Qualifier("urlsQueue")
    private BlockingQueue<String> urlsQueue;
    @Autowired
    @Qualifier("videoPagesQueue")
    private BlockingQueue<Document> videoPages;
    @Autowired
    private PageService pageService;
    @Autowired
    @Qualifier("parsedPagesUrls")
    private UrlsSet parsedPagesUrls;
    @Autowired
    @Qualifier("asyncVideoPageParserExecutor")
    private ThreadPoolTaskExecutor taskExecutor;
    @Value("${app.max.processing.queue.urls.size}")
    private int maxProcessingQueueUrlsSize;

    @Async
    public void start() {
        log.info("Video page parser statred");
        while (!isShouldBeTerminated()) {
            try {
                parse();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("Video page parser finished");
    }

    private boolean isShouldBeTerminated() {
        return taskExecutor.getThreadPoolExecutor().isShutdown();
    }

    private void parse() {
        log.debug("Parsing started");
        log.debug("Queue size " + videoPages.size());
        Document page = null;
        try {
            page = videoPages.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error occured before parsing document " + page.getUrl(), e);
        }
        if (parsedPagesUrls.isContains(page.getUrl())) {
            log.debug("Parsing page url " + page.getUrl() + " skipped. Already processed.");
            return;
        }
        log.debug("Parsing page url " + page.getUrl() + " started");
        Set<String> nextUrls;
        try {
            nextUrls = pageService.parseVideoPage(page);
            parsedPagesUrls.add(page.getUrl());
        } catch (Exception e) {
            urlsQueue.add(page.getUrl());
            throw e;
        }
        if (nextUrls != null && urlsQueue.size() < maxProcessingQueueUrlsSize) {
            urlsQueue.addAll(nextUrls);
        }
        log.debug("Parsing finished");
    }
}
