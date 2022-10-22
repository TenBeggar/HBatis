package com.tenbeggar.hbatis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;

import javax.annotation.PreDestroy;
import java.io.IOException;

public class HBaseTemplate {

    private String zookeeperQuorum;
    private String clientPort;
    private String znodeParent;

    private Configuration configuration;
    private Connection connection;
    private AggregationClient aggregationClient;
    private Admin admin;

    public HBaseTemplate() {
    }

    public HBaseTemplate(String zookeeperQuorum, String clientPort, String znodeParent) {
        this.zookeeperQuorum = zookeeperQuorum;
        this.clientPort = clientPort;
        this.znodeParent = znodeParent;
        this.configuration = this.configuration(this.zookeeperQuorum, this.clientPort, this.znodeParent);
        this.connection = this.connection(this.configuration);
        this.aggregationClient = this.aggregationClient(this.configuration);
        this.admin = this.admin(this.connection);
    }

    public Configuration configuration(String zookeeperQuorum, String clientPort, String znodeParent) {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", zookeeperQuorum);
        configuration.set("hbase.zookeeper.property.clientPort", clientPort);
        configuration.set("zookeeper.znode.parent", znodeParent);
        return configuration;
    }

    public Connection connection(Configuration configuration) {
        try {
            return ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            throw new RuntimeException("HBase Connection socket connect fail.");
        }
    }

    public AggregationClient aggregationClient(Configuration configuration) {
        return new AggregationClient(configuration);
    }

    public Admin admin(Connection connection) {
        try {
            return connection.getAdmin();
        } catch (IOException e) {
            throw new RuntimeException("HBase Admin get fail.");
        }
    }

    @PreDestroy
    public void close() {
        try {
            getAggregationClient().close();
            getAdmin().close();
            getConnection().close();
        } catch (IOException e) {
            throw new RuntimeException("HBase Connection socket close fail.");
        }
    }

    public String getZookeeperQuorum() {
        return zookeeperQuorum;
    }

    public void setZookeeperQuorum(String zookeeperQuorum) {
        this.zookeeperQuorum = zookeeperQuorum;
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    public String getZnodeParent() {
        return znodeParent;
    }

    public void setZnodeParent(String znodeParent) {
        this.znodeParent = znodeParent;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public AggregationClient getAggregationClient() {
        return aggregationClient;
    }

    public void setAggregationClient(AggregationClient aggregationClient) {
        this.aggregationClient = aggregationClient;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
