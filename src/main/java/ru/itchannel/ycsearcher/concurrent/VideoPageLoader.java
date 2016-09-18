package ru.itchannel.ycsearcher.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.dao.parser.Document;
import ru.itchannel.ycsearcher.distribute.UrlsSet;
import ru.itchannel.ycsearcher.service.PageService;

import java.util.concurrent.BlockingQueue;

@Component
@Scope("prototype")
public class VideoPageLoader {

    private static final Logger log = LoggerFactory.getLogger(VideoPageLoader.class);
    @Autowired
    @Qualifier("urlsQueue")
    private BlockingQueue<String> urlsQueue;
    @Autowired
    @Qualifier("videoPagesQueue")
    private BlockingQueue<Document> videoPages;
    @Autowired
    private PageService pageService;
    @Autowired
    @Qualifier("loadedPagesUrls")
    private UrlsSet loadedPagesUrls;
    @Autowired
    @Qualifier("asyncVideoPageLoaderExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Async
    public void start() {
        log.info("Video page loader statred");
        while (!isShouldBeTerminated()) {
            try {
                load();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("Video page loader finished");
    }

    private boolean isShouldBeTerminated() {
        return taskExecutor.getThreadPoolExecutor().isShutdown();
    }

    private void load() {
        log.debug("loading started");
        log.debug("Queue size " + urlsQueue.size());
        String pageUrl = null;
        try {
            pageUrl = urlsQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error occured before loading url " + pageUrl, e);
        }
        if (loadedPagesUrls.isContains(pageUrl)) {
            log.debug("Loading page url " + pageUrl + " skipped. Already processed.");
            return;
        }
        log.debug("Loading page url " + pageUrl + " started");
        try {
            Document document = pageService.getDocument(pageUrl);
            loadedPagesUrls.add(pageUrl);
            videoPages.put(document);
        } catch (Exception e) {
            urlsQueue.add(pageUrl);
            throw new RuntimeException("Loading page url " + pageUrl + " error", e);
        }
        log.debug("loading finished");
    }
}
