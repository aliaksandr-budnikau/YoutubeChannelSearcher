package ru.itchannel.ycsearcher.dao.parser;

import java.util.function.Consumer;

public interface Elements {
    boolean isEmpty();

    Element get(int i);

    String getUrl();

    void forEach(Consumer<? super Element> action);
}
