package ru.itchannel.ycsearcher.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itchannel.ycsearcher.dao.VideoPageDao;
import ru.itchannel.ycsearcher.dao.exception.HiddenSubscribersCounterException;
import ru.itchannel.ycsearcher.dao.parser.Document;
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.ChannelDto;
import ru.itchannel.ycsearcher.service.PageService;

import java.util.Set;

@Service
public class PageServiceImpl implements PageService {
    private static final Logger log = LoggerFactory.getLogger(PageServiceImpl.class);
    @Autowired
    private VideoPageDao videoPageDao;

    @Autowired
    private ChannelPool channelPool;

    @Override
    public Set<String> processVideoPage(String pageUrl) {
        Document document = videoPageDao.getDocument(pageUrl);
        String channelUrl = videoPageDao.getChannelUrl(document);
        if (channelPool.isNotExists(channelUrl)) {
            ChannelDto channelDto = new ChannelDto();
            channelDto.setUrl(channelUrl);
            channelDto.setName(videoPageDao.getChannelName(document));
            try {
                channelDto.setSubscribersCount(videoPageDao.getChannelSubscribersCount(document));
            } catch (HiddenSubscribersCounterException e) {
                log.info(e.getMessage(), e);
                channelDto.setSubscribersCount(-1);
            }
            channelPool.put(channelDto);
            log.info(String.valueOf(channelDto));
            log.info(String.valueOf(channelPool.size()));
        }
        return videoPageDao.getNextUrls(document);
    }
}
