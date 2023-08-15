package com.jooany.letsdeal.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSendReq {
    private Long saleId;
    private Long sellerId;
    private String messageContent;
    private Long messageGroupId;
    private Long opponentId;
    private Long userId;
}
