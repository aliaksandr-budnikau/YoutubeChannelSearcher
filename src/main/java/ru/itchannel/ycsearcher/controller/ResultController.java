package ru.itchannel.ycsearcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.Channel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ResultController {

    @Autowired
    private ChannelPool channelPool;

    @GetMapping("/result")
    public String findAll() {
        Map<String, Object> map = new HashMap<>();
        Collection<Channel> all = channelPool.findAll();
        map.put("count", all.size());
        map.put("channels", all);
        return map.toString();
    }
}
