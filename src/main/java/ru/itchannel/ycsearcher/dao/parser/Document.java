package ru.itchannel.ycsearcher.dao.parser;

public interface Document {

    Elements select(String selector);

    String getUrl();
}
