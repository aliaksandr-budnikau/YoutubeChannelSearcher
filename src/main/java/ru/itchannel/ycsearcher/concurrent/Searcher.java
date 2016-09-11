package ru.itchannel.ycsearcher.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itchannel.ycsearcher.service.PageService;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Searcher implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Searcher.class);
    private final int maxPocessingQueueuSize;
    private final BlockingQueue<String> queue;
    private final PageService pageService;
    private boolean shouldBeTerminated = false;

    public Searcher(BlockingQueue<String> queue, PageService pageService, int maxPocessingQueueuSize) {
        this.queue = queue;
        this.pageService = pageService;
        this.maxPocessingQueueuSize = maxPocessingQueueuSize;
    }

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
        Set<String> nextUrls = null;
        String pageUrl = null;
        try {
            pageUrl = queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error occured before processing url " + pageUrl, e);
        }
        log.info("Processing page url " + pageUrl + " started");
        try {
            nextUrls = pageService.processVideoPage(pageUrl);
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
