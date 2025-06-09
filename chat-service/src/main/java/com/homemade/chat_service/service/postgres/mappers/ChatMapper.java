/*
package com.homemade.chat_service.service.postgres.mappers;

import com.homemade.chat_service.model.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ChatMapper {

    */
/**
     * chat messages that should be recieved
     * @param userId
     * @return
     *//*

    @Select("select from , to, txt, sent_ts from chat_system.messages where to = #{userId} and sent_ts > #{ts}")
    @Results(
            {
                    @Result(property = "from", column = "from"),
                    @Result(property = "to", column = "to"),
                    @Result(property = "txt", column = "txt"),
                    @Result(property = "sentTs", column = "sent_ts")
            }
    )
    List<ChatMessage> findChatMessagesFor(@Param("userId") int userId, @Param("ts") LocalDateTime ts);

    @Insert("insert into chat_system.messages (from, to, txt, sent_ts) values (#{from}, #{to}, #{txt}, #{sent_ts})")
    void insertMessage(
            @Param("#{from}") String from,
            @Param("#{to}") String to,
            @Param("#{txt}") String txt,
            @Param("#{sent_ts}") LocalDateTime sentTs
    );
}
*/
