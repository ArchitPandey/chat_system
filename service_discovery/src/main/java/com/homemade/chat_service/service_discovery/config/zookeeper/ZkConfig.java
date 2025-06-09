package com.homemade.chat_service.service_discovery.config.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkConfig {

    @Bean
    CuratorFramework zkClient() {
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString("localhost:31598")
                .sessionTimeoutMs(15000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        zkClient.start();
        return zkClient;
    }

}
