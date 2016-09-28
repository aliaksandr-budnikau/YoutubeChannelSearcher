package ru.itchannel.ycsearcher.distribute.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.ChannelDto;

import java.util.Collection;
import java.util.Map;

@Component
public class ChannelPoolImpl implements ChannelPool {

    @Autowired
    @Qualifier("channelPool")
    private Map<String, ChannelDto> channels;

    @Override
    public ChannelDto findByUrl(String url) {
        return channels.get(url);
    }

    @Override
    public boolean isNotExists(String url) {
        return !channels.containsKey(url);
    }

    @Override
    public void put(ChannelDto channelDto) {
        channels.put(channelDto.getUrl(), channelDto);
    }

    @Override
    public Collection<ChannelDto> findAll() {
        return channels.values();
    }

    @Override
    public int size() {
        return channels.size();
    }
}
