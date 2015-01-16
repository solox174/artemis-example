create keyspace hydra with replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
create table hydra.exampleservice (
    accountId ascii,
    deviceId ascii,
    data ascii,
    PRIMARY KEY (accountId, deviceId)
);
