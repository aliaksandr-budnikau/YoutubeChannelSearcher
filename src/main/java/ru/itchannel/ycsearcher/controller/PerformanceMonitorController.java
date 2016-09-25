package ru.itchannel.ycsearcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itchannel.ycsearcher.concurrent.PerformanceMonitor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
public class PerformanceMonitorController {

    @Autowired
    private PerformanceMonitor monitor;

    @GetMapping("/performance")
    public HashMap getPerformanceSummaryHistory() {
        List<Double> speed = new LinkedList<>();
        List<Integer> quantity = new LinkedList<>();
        monitor.getPerformanceSummaryHistory().forEach(it -> {
            quantity.add(it.getChannelsCount());
            speed.add(it.getCurrentSpeed());
        });
        HashMap map = new HashMap();
        map.put("speed", speed);
        map.put("quantity", quantity);
        return map;
    }
}
