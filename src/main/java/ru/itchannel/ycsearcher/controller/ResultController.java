package ru.itchannel.ycsearcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itchannel.ycsearcher.distribute.ChannelPool;
import ru.itchannel.ycsearcher.dto.Channel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ResultController {


    @Autowired
    private ChannelPool channelPool;

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public String findAll() {
        Map<String, Object> map = new HashMap<>();
        Collection<Channel> all = channelPool.findAll();
        all.size();
        map.put("count", all.size());
        map.put("channels", all);
        return map.toString();
    }
}
