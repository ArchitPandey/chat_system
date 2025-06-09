package com.homemade.chat_service.service.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WebSocketSessionManager {

    Map<String, WebSocketSession> sessionMap;

    public WebSocketSessionManager() {
        this.sessionMap = new ConcurrentHashMap<>();
    }

    public void addWebSocketSession(String userId, WebSocketSession session) {
        sessionMap.put(userId, session);
    }

    public void removeWebSocketSession(String userId) {
        sessionMap.remove(userId);
    }

    public WebSocketSession getWebSocketSession(String userId) {
        return sessionMap.get(userId);
    }

}
