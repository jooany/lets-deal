package com.jooany.letsdeal.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageAllDeleteReq {
    private Long messageGroupId;
    private List<Long> messageIds;
    private Long opponentId;
    private Long userId;
}
