package ru.itchannel.ycsearcher.distribute;

public interface UrlsSet {
    boolean isContains(String url);

    void add(String url);
}
