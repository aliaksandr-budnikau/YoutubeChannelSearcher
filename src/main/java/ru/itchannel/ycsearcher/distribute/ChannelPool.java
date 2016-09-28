package ru.itchannel.ycsearcher.distribute;

import ru.itchannel.ycsearcher.dto.ChannelDto;

import java.util.Collection;

public interface ChannelPool {

    ChannelDto findByUrl(String url);

    boolean isNotExists(String url);

    void put(ChannelDto channelDto);

    Collection<ChannelDto> findAll();

    int size();
}
