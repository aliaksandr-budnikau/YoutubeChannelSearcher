package ru.itchannel.ycsearcher.dao.parser.impl;

import ru.itchannel.ycsearcher.dao.parser.Connection;
import ru.itchannel.ycsearcher.dao.parser.Document;

import java.io.IOException;

public class ConnectionImpl implements Connection {
    private final org.jsoup.Connection connect;
    private final String url;

    public ConnectionImpl(org.jsoup.Connection connect, String url, int timeout) {
        this.connect = connect;
        this.url = url;
        connect.timeout(timeout);
    }

    @Override
    public Document get() throws IOException {
        return new DocumentImpl(connect.get(), url);
    }
}
