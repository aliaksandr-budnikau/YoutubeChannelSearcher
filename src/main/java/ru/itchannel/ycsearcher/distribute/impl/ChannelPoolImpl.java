package ru.itchannel.ycsearcher.distribute.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.Channel;

import java.util.Collection;
import java.util.Map;

@Component
public class ChannelPoolImpl implements ChannelPool {

    @Autowired
    @Qualifier("channelPool")
    private Map<String, Channel> channels;

    @Override
    public Channel findByUrl(String url) {
        return channels.get(url);
    }

    @Override
    public boolean isNotExists(String url) {
        return !channels.containsKey(url);
    }

    @Override
    public void put(Channel channel) {
        channels.put(channel.getUrl(), channel);
    }

    @Override
    public Collection<Channel> findAll() {
        return channels.values();
    }

    @Override
    public int size() {
        return channels.size();
    }
}
