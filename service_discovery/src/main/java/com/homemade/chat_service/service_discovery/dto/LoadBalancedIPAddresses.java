package com.homemade.chat_service.service_discovery.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class LoadBalancedIPAddresses {

    List<WebSocketTraffic> sortedTraffic;

    String sortOrder;

    public LoadBalancedIPAddresses(List<WebSocketTraffic> traffic) {
        Collections.sort(traffic);
        this.sortedTraffic = traffic;
        this.sortOrder = "ASC";
    }

}
