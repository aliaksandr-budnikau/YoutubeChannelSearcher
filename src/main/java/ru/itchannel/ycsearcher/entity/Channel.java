package ru.itchannel.ycsearcher.entity;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Objects;

@Table(value = "channels")
public class Channel {
    @PrimaryKey
    private String url;
    private long subscribersCount;
    private String name;

    public long getSubscribersCount() {
        return subscribersCount;
    }

    public void setSubscribersCount(long subscribersCount) {
        this.subscribersCount = subscribersCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(subscribersCount, channel.subscribersCount) &&
                Objects.equals(name, channel.name) &&
                Objects.equals(url, channel.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscribersCount, name, url);
    }

    @Override
    public String toString() {
        return "ChannelDto{" +
                "subscribersCount=" + subscribersCount +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
