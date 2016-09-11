package ru.itchannel.ycsearcher.dao.parser.impl;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.dao.parser.Connection;
import ru.itchannel.ycsearcher.dao.parser.ConnectionFactory;

@Component
public class ConnectionFactoryImpl implements ConnectionFactory{

    @Override
    public Connection getConnection(String url, int timeout) {
        return new ConnectionImpl(Jsoup.connect(url), url, timeout);
    }
}
