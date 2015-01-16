package net.disbelieve.wicca.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by kmatth002c on 12/10/2014.
 */
@Table(keyspace = "hydra", name = "exampleservice")
@XmlRootElement
public class WiccaData {
    @PartitionKey
    private String accountId;
    @ClusteringColumn
    private String deviceId;
    private String data;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
