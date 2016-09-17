package ru.itchannel.ycsearcher.distribute.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.distribute.VisitedUrlsSet;

import java.util.Map;

@Component
public class VisitedUrlsSetImpl implements VisitedUrlsSet {

    @Autowired
    @Qualifier("visitedUrlsMap")
    private Map<String, Object> map;

    @Override
    public boolean isContains(String url) {
        return map.containsKey(url);
    }

    @Override
    public void add(String url) {
        map.put(url, new Object());
    }
}
