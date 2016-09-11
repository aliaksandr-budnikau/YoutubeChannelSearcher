package ru.itchannel.ycsearcher.dao.parser.impl;

import ru.itchannel.ycsearcher.dao.parser.Document;
import ru.itchannel.ycsearcher.dao.parser.Elements;

import java.util.Objects;

public class DocumentImpl implements Document {

    private final org.jsoup.nodes.Document document;
    private final String url;

    public DocumentImpl(org.jsoup.nodes.Document document, String url) {
        this.document = document;
        this.url = url;
    }

    @Override
    public Elements select(String selector) {
        return new ElementsImpl(document.select(selector), url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentImpl document1 = (DocumentImpl) o;
        return Objects.equals(document, document1.document) &&
                Objects.equals(url, document1.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, url);
    }

    @Override
    public String toString() {
        return "DocumentImpl{" +
                "url='" + url + '\'' +
                '}';
    }
}
