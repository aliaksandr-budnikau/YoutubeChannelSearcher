package ru.itchannel.ycsearcher.dao;

import ru.itchannel.ycsearcher.dao.exception.HiddenSubscribersCounterException;
import ru.itchannel.ycsearcher.dao.parser.Document;

import java.util.Set;

public interface VideoPageDao {

    long getChannelSubscribersCount(Document document) throws HiddenSubscribersCounterException;

    String getChannelName(Document document);

    String getChannelUrl(Document document);

    Document getDocument(String pageUrl);

    Set<String> getNextUrls(Document document);
}
