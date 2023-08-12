package com.jooany.letsdeal.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
public class MessageGroupRes {
    private Long id;
    private Long saleId;
    private String thumbnailImageUrl;
    private String opponentName;
    private Timestamp opponentDeletedAt;
    private String lastMessageContent;
    private Timestamp lastMessageRegisteredAt;
    private Integer unreadMessageCount;
    private Timestamp registeredAt;
}
