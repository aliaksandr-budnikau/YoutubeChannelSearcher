CREATE KEYSPACE IF NOT EXISTS channelSearcher
WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 3 }
AND DURABLE_WRITES = false;

create table channelSearcher.channels (url text primary key, subscribersCount bigint, name text);