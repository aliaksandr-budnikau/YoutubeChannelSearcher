package ru.itchannel.ycsearcher.distribute;

import ru.itchannel.ycsearcher.dto.Channel;

import java.util.Collection;

public interface ChannelPool {

    Channel findByUrl(String url);

    boolean isNotExists(String url);

    void put(Channel channel);

    Collection<Channel> findAll();

    int size();
}
