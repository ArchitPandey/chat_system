package com.homemade.chat_service.service.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

@Service
@Slf4j
public class ZkService {

    CuratorFramework zkClient;
    String ipAddress;

    public ZkService(CuratorFramework zkClient) throws UnknownHostException {
        this.zkClient = zkClient;
        this.ipAddress = InetAddress.getLocalHost().getHostAddress();
    }

    public void registerConnection(int connectionCount) {
        try {
            String path = """
                /chat-service/%s""".formatted(this.ipAddress);
            String metadata = """
                { "host": "%s", "connections": "%d" }"""
                    .formatted(
                            this.ipAddress,
                            connectionCount
                    );
            Stat pathStat = this.zkClient.checkExists().forPath(path);

            if (Objects.nonNull(pathStat)) {
                this.zkClient
                        .setData()
                        .forPath(path, metadata.getBytes());
            } else {
                this.zkClient
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(path, metadata.getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
