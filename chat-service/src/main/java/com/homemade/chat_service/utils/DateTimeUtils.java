package com.homemade.chat_service.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static String localDateTimeToString(LocalDateTime ts, String format) {
        return ts.format(
                DateTimeFormatter.ofPattern(format)
        );
    }

    public static LocalDateTime stringToLocalDateTime(String dateString, String format) {
        return LocalDateTime.parse(
                dateString,
                DateTimeFormatter.ofPattern(format)
        );
    }

}
