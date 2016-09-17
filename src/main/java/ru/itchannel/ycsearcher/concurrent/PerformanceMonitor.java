package ru.itchannel.ycsearcher.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.PerformanceSummary;

import javax.annotation.PostConstruct;

@Component
public class PerformanceMonitor {

    public static final int MILLIS_PER_SECOND = 1000;
    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitor.class);
    private static final Object object = new Object();
    @Autowired
    private ChannelPool channelPool;
    @Value("${app.rate.between.result.snapshots}")
    private int rateBetweenResultSnapshots;
    private PerformanceSummary summarySnapshot;
    private PerformanceSummary summary;

    @PostConstruct
    public void init() {
        log.info("Init performance monitor");
        summary = new PerformanceSummary();
        summarySnapshot = new PerformanceSummary();
    }

    @Scheduled(fixedDelayString = "${app.perfomance.monitor.execution.delay}")
    public void start() {
        try {
            benchmark();
            synchronized (object) {
                makeSnapshot();
            }
        } catch (Throwable t) {
            log.error("Error: ", t);
        }
        log.info("Stop performance monitor");
    }

    private void makeSnapshot() {
        summarySnapshot.setMinSpeed(summary.getMinSpeed());
        summarySnapshot.setMaxSpeed(summary.getMaxSpeed());
        summarySnapshot.setCurrentSpeed(summary.getCurrentSpeed());
    }

    private void benchmark() throws InterruptedException {
        int startChannelCount = channelPool.size();
        long startTimeInMillis = System.currentTimeMillis();
        Thread.sleep(rateBetweenResultSnapshots);
        int endChannelCount = channelPool.size();
        long endTimeInMillis = System.currentTimeMillis();

        double currentSpeed = (endChannelCount - startChannelCount) /
                (double) ((endTimeInMillis - startTimeInMillis) / MILLIS_PER_SECOND);
        if (summary.getMinSpeed() == 0) {
            summary.setMinSpeed(currentSpeed);
        }
        if (summary.getMaxSpeed() == 0) {
            summary.setMaxSpeed(currentSpeed);
        }
        summary.setMinSpeed(Math.min(summary.getMinSpeed(), currentSpeed));
        summary.setMaxSpeed(Math.max(summary.getMaxSpeed(), currentSpeed));
        summary.setCurrentSpeed(currentSpeed);
        log.info("Performance summary {}", summary);
    }

    public PerformanceSummary getSummary() {
        synchronized (object) {
            log.info("Get summary {}", summarySnapshot);
            return summarySnapshot;
        }
    }
}
