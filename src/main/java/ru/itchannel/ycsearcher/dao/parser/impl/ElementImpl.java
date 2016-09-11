package ru.itchannel.ycsearcher.dao.parser.impl;

import ru.itchannel.ycsearcher.dao.parser.Element;

public class ElementImpl implements Element {
    private final org.jsoup.nodes.Element element;

    public ElementImpl(org.jsoup.nodes.Element element) {
        this.element = element;
    }

    @Override
    public String attr(String attr) {
        return element.attr(attr);
    }

    @Override
    public String text() {
        return element.text();
    }
}
