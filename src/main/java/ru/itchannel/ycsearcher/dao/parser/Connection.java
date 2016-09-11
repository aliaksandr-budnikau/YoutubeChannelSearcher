package ru.itchannel.ycsearcher.dao.parser;

import java.io.IOException;

public interface Connection {

    Document get() throws IOException;
}
