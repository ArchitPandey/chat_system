package com.homemade.chat_service.service.websocket;

import com.homemade.chat_service.utils.DateTimeUtils;

import java.time.LocalDateTime;

public class WebSocketMessageUtility {

    public static String getToUserFromChatMessage(String txtMessage) {
        String[] splitMessage = txtMessage.split("\\|");
        return splitMessage[1];
    }

    public static String createPlainTextMessage(String from, String to, String message, LocalDateTime sentTs) {
        String sentTsString = DateTimeUtils.localDateTimeToString(sentTs, "yyyy-MM-dd HH:mm:ss");
        return new StringBuilder()
                .append(from)
                .append("|")
                .append(to)
                .append("|")
                .append(message)
                .append("|")
                .append(sentTsString)
                .toString();
    }

}
