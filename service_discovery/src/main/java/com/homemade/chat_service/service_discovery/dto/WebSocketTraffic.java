package com.homemade.chat_service.service_discovery.dto;

import lombok.*;

import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WebSocketTraffic implements Comparable<WebSocketTraffic> {

    String host;

    int connections;

    @Override
    public int compareTo(WebSocketTraffic o) {
        return this.getConnections() - o.getConnections();
    }
}
