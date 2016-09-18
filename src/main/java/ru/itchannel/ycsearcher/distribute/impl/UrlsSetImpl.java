package ru.itchannel.ycsearcher.distribute.impl;

import ru.itchannel.ycsearcher.distribute.UrlsSet;

import java.util.Map;

public class UrlsSetImpl implements UrlsSet {

    private Map<String, Object> map;

    public UrlsSetImpl(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public boolean isContains(String url) {
        return map.containsKey(url);
    }

    @Override
    public void add(String url) {
        map.put(url, new String());
    }
}
