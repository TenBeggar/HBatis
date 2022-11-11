package com.tenbeggar.hbatis.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Import(HBaseProperties.class)
public class HBaseTemplate {

    private static final String HBATIS_V = "2.4.12";

    @Autowired
    private HBaseProperties hbaseProperties;

    private Configuration configuration;
    private Connection connection;
    private AggregationClient aggregationClient;
    private Admin admin;

    public HBaseTemplate() {
    }

    @PostConstruct
    public void init() {
        this.configuration = this.configuration(this.hbaseProperties);
        this.connection = this.connection(this.configuration);
        this.aggregationClient = this.aggregationClient(this.configuration);
        this.admin = this.admin(this.connection);
        System.out.print(
                "*=====================================*\n" +
                        " |     |-----|              *  |-----|\n" +
                        " |     |     |        |     |  |      \n" +
                        " |-----|-----|-----|--|--|  |  |-----|\n" +
                        " |     |     |     |  |     |        |\n" +
                        " |     |-----|-----|  |__|  |  |-----|\n" +
                        "*=====================================*\n" +
                        "                        HBatis v+" + HBATIS_V + "+\n"
        );
    }

    public Configuration configuration(HBaseProperties hbaseProperties) {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", hbaseProperties.getZookeeperQuorum());
        configuration.set("hbase.zookeeper.property.clientPort", hbaseProperties.getClientPort());
        configuration.set("zookeeper.znode.parent", hbaseProperties.getZnodeParent());
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

    public HBaseProperties getHbaseProperties() {
        return hbaseProperties;
    }

    public void setHbaseProperties(HBaseProperties hbaseProperties) {
        this.hbaseProperties = hbaseProperties;
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
