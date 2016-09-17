package ru.itchannel.ycsearcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itchannel.ycsearcher.concurrent.PerformanceMonitor;
import ru.itchannel.ycsearcher.dto.PerformanceSummary;

import java.util.HashMap;

@Controller
public class PerformanceMonitorController {

    @Autowired
    private PerformanceMonitor monitor;

    @RequestMapping(value = "/performance", method = RequestMethod.GET)
    @ResponseBody
    public String findAll() {
        PerformanceSummary summary = monitor.getSummary();
        HashMap<String, Double> map = new HashMap<>();
        map.put("currentSpeed", summary.getCurrentSpeed());
        map.put("maxSpeed", summary.getMaxSpeed());
        map.put("minSpeed", summary.getMinSpeed());
        return map.toString();
    }
}
