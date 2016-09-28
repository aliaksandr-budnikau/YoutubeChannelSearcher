package ru.itchannel.ycsearcher.repository;

import com.hazelcast.core.MapStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itchannel.ycsearcher.dto.ChannelDto;
import ru.itchannel.ycsearcher.entity.Channel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ChannelMapStore implements MapStore<String, ChannelDto> {

    @Autowired
    private ChannelRepository repository;

    @Override
    public ChannelDto load(String url) {
        Channel channel = repository.findOne(url);
        return channel == null ? null : map(channel);
    }

    private ChannelDto map(Channel channel) {
        ChannelDto channelDto = new ChannelDto();
        channelDto.setName(channel.getName());
        channelDto.setUrl(channel.getUrl());
        channelDto.setSubscribersCount(channel.getSubscribersCount());
        return channelDto;
    }

    private Channel map(ChannelDto channelDto) {
        Channel channel = new Channel();
        channel.setName(channelDto.getName());
        channel.setUrl(channelDto.getUrl());
        channel.setSubscribersCount(channelDto.getSubscribersCount());
        return channel;
    }

    @Override
    public Map<String, ChannelDto> loadAll(Collection<String> collection) {
        HashMap<String, ChannelDto> map = new HashMap<>();
        repository.findAll().forEach(it -> {
            map.put(it.getUrl(), map(it));
        });
        return map;
    }

    @Override
    public Iterable<String> loadAllKeys() {
        return repository.findAllUrls();
    }

    @Override
    public void store(String url, ChannelDto channel) {
        repository.save(map(channel));
    }

    @Override
    public void storeAll(Map<String, ChannelDto> map) {
        List<Channel> entities = map.values().stream().map(channel -> map(channel)).collect(Collectors.toList());
        repository.save(entities);
    }

    @Override
    public void delete(String url) {
        repository.delete(url);
    }

    @Override
    public void deleteAll(Collection<String> urls) {
        urls.forEach(url -> {
            repository.delete(url);
        });
    }
}
