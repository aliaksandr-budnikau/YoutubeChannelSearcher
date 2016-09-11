package ru.itchannel.ycsearcher.distribute.impl;

import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.Channel;

import java.util.Collection;
import java.util.Map;

public class ChannelPoolImpl implements ChannelPool {

    private Map<String, Channel> channels;

    public ChannelPoolImpl(Map<String, Channel> channels) {
        this.channels = channels;
    }

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
