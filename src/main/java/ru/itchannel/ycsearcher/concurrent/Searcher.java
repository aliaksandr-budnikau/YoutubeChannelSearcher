package ru.itchannel.ycsearcher.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.distribute.VisitedUrlsSet;
import ru.itchannel.ycsearcher.service.PageService;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

@Component
@Scope("prototype")
public class Searcher implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Searcher.class);
    @Autowired
    private BlockingQueue<String> queue;
    @Autowired
    private PageService pageService;
    @Autowired
    private VisitedUrlsSet visitedUrlsSet;
    @Value("${app.max.processing.queue.size}")
    private int maxPocessingQueueuSize;
    private boolean shouldBeTerminated = false;

    @Override
    public void run() {
        log.info("Searcher statred");
        while (!isShouldBeTerminated()) {
            try {
                processing();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("Searcher finished");
    }

    private boolean isShouldBeTerminated() {
        return shouldBeTerminated;
    }

    private void processing() {
        log.info("Processing started");
        log.info("Queue size " + queue.size());
        Set<String> nextUrls;
        String pageUrl = null;
        try {
            pageUrl = queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error occured before processing url " + pageUrl, e);
        }
        if (visitedUrlsSet.isContains(pageUrl)) {
            log.info("Processing page url " + pageUrl + " skipped. Already processed.");
            return;
        }
        log.info("Processing page url " + pageUrl + " started");
        try {
            nextUrls = pageService.processVideoPage(pageUrl);
            visitedUrlsSet.add(pageUrl);
        } catch (Exception e) {
            queue.add(pageUrl);
            throw e;
        }
        if (nextUrls != null && queue.size() < maxPocessingQueueuSize) {
            queue.addAll(nextUrls);
        }
        log.info("Processing finished");
    }
}
