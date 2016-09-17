package ru.itchannel.ycsearcher.distribute;

public interface VisitedUrlsSet {
    boolean isContains(String url);

    void add(String url);
}
