package com.tenbeggar.hbatis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "hbase")
public class HBaseProperties {

    private static final String DEFAULT_ZOOKEEPER_QUORUM = "localhost";
    private static final String DEFAULT_CLIENT_PORT = "2181";
    private static final String DEFAULT_ZNODE_PARENT = "/hbase";

    private static final String ZOOKEEPER_QUORUM_KEY = "quorum";
    private static final String CLIENT_PORT_KEY = "property.clientport";
    private static final String ZNODE_PARENT_KEY = "znode.parent";

    private Map<String, String> zookeeper = new HashMap<>();

    public Map<String, String> getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(Map<String, String> zookeeper) {
        this.zookeeper = zookeeper;
    }

    public String getZookeeperQuorum() {
        return zookeeper.getOrDefault(ZOOKEEPER_QUORUM_KEY, DEFAULT_ZOOKEEPER_QUORUM);
    }

    public String getClientPort() {
        return zookeeper.getOrDefault(CLIENT_PORT_KEY, DEFAULT_CLIENT_PORT);
    }

    public String getZnodeParent() {
        return zookeeper.getOrDefault(ZNODE_PARENT_KEY, DEFAULT_ZNODE_PARENT);
    }
}
