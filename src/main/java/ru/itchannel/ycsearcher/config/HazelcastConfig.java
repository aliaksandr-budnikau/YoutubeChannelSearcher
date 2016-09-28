package ru.itchannel.ycsearcher.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itchannel.ycsearcher.dto.ChannelDto;
import ru.itchannel.ycsearcher.repository.ChannelMapStore;

import java.util.Map;

@Configuration
public class HazelcastConfig {

    public static final String CHANNELS_MAP = "CHANNELS_MAP";
    public static final String VISITED_URLS_SET = "VISITED_URLS_SET";

    @Value("${app.hazelcast.storage.delay.seconds}")
    private int hazelcastStorageDelaySeconds;

    @Bean
    public Config config(MapConfig channelMapConfig) {
        Config config = new Config();
        Map<String, MapConfig> mapConfigs = config.getMapConfigs();
        mapConfigs.put(CHANNELS_MAP, channelMapConfig);
        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config config) {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        return instance;
    }

    @Bean
    public MapStoreConfig channelMapStoreConfig(ChannelMapStore channelMapStore) {
        MapStoreConfig config = new MapStoreConfig();
        config.setImplementation(channelMapStore);
        config.setWriteDelaySeconds(hazelcastStorageDelaySeconds);
        return config;
    }

    @Bean
    public MapConfig channelMapConfig(MapStoreConfig channelMapStoreConfig) {
        MapConfig config = new MapConfig();
        config.setMapStoreConfig(channelMapStoreConfig);
        return config;
    }

    @Bean
    public Map<String, ChannelDto> channelPool(HazelcastInstance hazelcast) {
        return hazelcast.getMap(CHANNELS_MAP);
    }

    @Bean
    public Map<String, Object> visitedUrlsMap(HazelcastInstance hazelcast) {
        return hazelcast.getMap(VISITED_URLS_SET);
    }
}
