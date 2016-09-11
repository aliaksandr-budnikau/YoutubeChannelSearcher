package ru.itchannel.ycsearcher.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itchannel.ycsearcher.dao.VideoPageDao;
import ru.itchannel.ycsearcher.dao.exception.HiddenSubscribersCounterException;
import ru.itchannel.ycsearcher.dao.parser.Connection;
import ru.itchannel.ycsearcher.dao.parser.ConnectionFactory;
import ru.itchannel.ycsearcher.dao.parser.Document;
import ru.itchannel.ycsearcher.dao.parser.Element;
import ru.itchannel.ycsearcher.dao.parser.Elements;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class VideoPageDaoImpl implements VideoPageDao {
    @Value("${app.url.youtube.host}")
    private String youtubeHost;
    @Value("${app.loading.page.timeout}")
    private int loadingPageTimeout;
    @Autowired
    private ConnectionFactory connectionFactory;

    @Override
    public long getChannelSubscribersCount(Document document) throws HiddenSubscribersCounterException {
        String selector = ".yt-uix-button-subscription-container .yt-subscriber-count";
        Elements elements = document.select(selector);
        if (elements.isEmpty()) {
            throw new HiddenSubscribersCounterException("Selector " + selector + " not found. On page " + elements.getUrl());
        }
        return convertToLong(elements.get(0));
    }

    @Override
    public String getChannelName(Document document) {
        String selector = ".yt-user-info a";
        Elements elements = document.select(selector);
        checkResult(selector, elements);
        return elements.get(0).text();
    }

    @Override
    public String getChannelUrl(Document document) {
        String selector = ".yt-user-info a";
        Elements elements = document.select(selector);
        checkResult(selector, elements);
        return youtubeHost + elements.get(0).attr("href");
    }

    public Document getDocument(String pageUrl) {
        Connection connect = connectionFactory.getConnection(pageUrl, loadingPageTimeout);
        try {
            return connect.get();
        } catch (IOException e) {
            throw new RuntimeException("Url '" + pageUrl + "'. Get document error", e);
        }
    }

    @Override
    public Set<String> getNextUrls(Document document) {
        String selector = "#watch-related li .content-wrapper a";
        Elements elements = document.select(selector);
        checkResult(selector, elements);
        Set<String> result = new LinkedHashSet<>();
        elements.forEach(element -> result.add(youtubeHost + element.attr("href")));
        return result;
    }

    private long convertToLong(Element element) {
        String pureNumber = String.join("", element.text().split(","));
        return Long.parseLong(pureNumber);
    }

    private void checkResult(String selector, Elements elements) {
        if (elements.isEmpty()) {
            String message = "Selector " + selector + " not found. On page " + elements.getUrl();
            throw new RuntimeException(message);
        }
    }
}
