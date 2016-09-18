package ru.itchannel.ycsearcher.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.PerformanceSummary;

import java.util.LinkedList;
import java.util.List;

@Component
public class PerformanceMonitor {

    public static final int MILLIS_PER_SECOND = 1000;
    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitor.class);
    @Autowired
    private ChannelPool channelPool;
    @Value("${app.rate.between.result.snapshots}")
    private int rateBetweenResultSnapshots;
    private PerformanceSummary summary = new PerformanceSummary();
    private List<PerformanceSummary> summaryHistory = new LinkedList<>();

    @Scheduled(fixedDelayString = "${app.perfomance.monitor.execution.delay}")
    public void start() {
        try {
            benchmark();
            summaryHistory.add(getCopy(summary));
        } catch (Throwable t) {
            log.error("Error: ", t);
        }
        log.info("Stop performance monitor");
    }

    private PerformanceSummary getCopy(PerformanceSummary source) {
        PerformanceSummary target = new PerformanceSummary();
        target.setMinSpeed(source.getMinSpeed());
        target.setMaxSpeed(source.getMaxSpeed());
        target.setCurrentSpeed(source.getCurrentSpeed());
        target.setChannelsCount(source.getChannelsCount());
        return target;
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
        summary.setChannelsCount(startChannelCount);
        log.info("Performance summary {}", summary);
    }

    public List<PerformanceSummary> getPerformanceSummaryHistory() {
        return summaryHistory;
    }
}
