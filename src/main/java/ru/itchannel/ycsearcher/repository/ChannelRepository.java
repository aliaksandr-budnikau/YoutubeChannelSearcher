package ru.itchannel.ycsearcher.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itchannel.ycsearcher.entity.Channel;

import java.util.List;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, String> {

    @Query("select * from channels")
    List<String> findAllUrls();
}
