package com.homemade.chat_service.service_discovery.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homemade.chat_service.service_discovery.dto.LoadBalancedIPAddresses;
import com.homemade.chat_service.service_discovery.dto.WebSocketTraffic;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat-system/service-discovery")
@Slf4j
public class ServiceDiscoveryApi {

    CuratorFramework zkClient;
    ObjectMapper objectMapper;

    public ServiceDiscoveryApi(CuratorFramework zkClient, ObjectMapper objectMapper) {
        this.zkClient = zkClient;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/chat-servers")
    public ResponseEntity<LoadBalancedIPAddresses> findAvailableChatServers() throws Exception {
        try {
            List<WebSocketTraffic> webSocketTrafficList = new ArrayList<>();

            List<String> registeredIPs = this.zkClient.getChildren().forPath("/chat-service");
            for(String ip: registeredIPs) {
                String path = """
                        /chat-service/%s""".formatted(ip);
                byte[] metadataBytes = this.zkClient.getData().forPath(path);
                WebSocketTraffic webSocketTraffic = this.objectMapper.readValue(metadataBytes, WebSocketTraffic.class);
                webSocketTrafficList.add(webSocketTraffic);
            }

            return ResponseEntity.ok(new LoadBalancedIPAddresses(webSocketTrafficList));

        } catch (Exception e) {
            log.error("error during service discovery for chat-websocket-ips", e);
            throw e;
        }

    }

}
