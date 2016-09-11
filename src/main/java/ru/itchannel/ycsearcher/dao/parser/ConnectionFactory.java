package ru.itchannel.ycsearcher.dao.parser;

public interface ConnectionFactory {

    Connection getConnection(String url, int timeout);
}
