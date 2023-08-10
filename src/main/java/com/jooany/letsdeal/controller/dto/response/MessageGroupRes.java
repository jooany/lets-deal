package com.jooany.letsdeal.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
public class MessageGroupRes {
    private Long id;
    private Long saleId;
    private String thumbnailImageUrl;
    private String opponentName;
    private String lastMessageContent;
    private Timestamp lastMessageRegisteredAt;
    private Integer unreadMessageCount;
    private Timestamp registeredAt;

    @QueryProjection
    public MessageGroupRes(Long id, Long saleId, String thumbnailImageUrl, String opponentName, String lastMessageContent, Timestamp lastMessageRegisteredAt, Integer unreadMessageCount, Timestamp registeredAt) {
        this.id = id;
        this.saleId = saleId;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.opponentName = opponentName;
        this.lastMessageContent = lastMessageContent;
        this.lastMessageRegisteredAt = lastMessageRegisteredAt;
        this.unreadMessageCount = unreadMessageCount;
        this.registeredAt = registeredAt;
    }
}
