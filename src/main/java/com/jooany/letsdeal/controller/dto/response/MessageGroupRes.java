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
    private String title;
    private String thumbnailImageUrl;
    private Boolean wasSaleDeleted;
    private Long opponentId;
    private String opponentName;
    private String lastMessageContent;
    private Timestamp lastMessageRegisteredAt;
    private Integer unreadMessageCount;
    private Timestamp registeredAt;
}