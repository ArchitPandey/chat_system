package com.homemade.chat_service.config.websocket;

import com.homemade.chat_service.service.websocket.ChatMessagesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WSConfig {

    ChatMessagesHandler chatMessagesHandler;

    public WSConfig(ChatMessagesHandler chatMessagesHandler) {
        this.chatMessagesHandler = chatMessagesHandler;
    }

    @Bean
    public HandlerMapping wsMapping() {
        Map<String, WebSocketHandler> wsMap = new HashMap<>();
        wsMap.put("/chat-ws", this.chatMessagesHandler);

        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setOrder(10);
        simpleUrlHandlerMapping.setUrlMap(wsMap);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter wsHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }


}
