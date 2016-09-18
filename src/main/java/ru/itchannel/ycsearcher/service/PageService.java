package ru.itchannel.ycsearcher.service;

import ru.itchannel.ycsearcher.dao.parser.Document;

import java.util.Set;

public interface PageService {
    Set<String> parseVideoPage(Document document);

    Document getDocument(String pageUrl);
}
