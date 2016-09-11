package ru.itchannel.ycsearcher.dao.parser.impl;

import ru.itchannel.ycsearcher.dao.parser.Element;
import ru.itchannel.ycsearcher.dao.parser.Elements;

import java.util.Objects;
import java.util.function.Consumer;

public class ElementsImpl implements Elements {
    private final org.jsoup.select.Elements elements;
    private final String url;

    public ElementsImpl(org.jsoup.select.Elements elements, String url) {
        this.elements = elements;
        this.url = url;
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public Element get(int i) {
        return new ElementImpl(elements.get(i));
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void forEach(Consumer<? super Element> action) {
        elements.stream().map(element -> new ElementImpl(element)).forEach(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementsImpl elements1 = (ElementsImpl) o;
        return Objects.equals(elements, elements1.elements) &&
                Objects.equals(url, elements1.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements, url);
    }

    @Override
    public String toString() {
        return "ElementsImpl{" +
                "url='" + url + '\'' +
                '}';
    }
}
