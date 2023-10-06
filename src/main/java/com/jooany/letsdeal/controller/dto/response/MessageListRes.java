package com.jooany.letsdeal.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MessageListRes {
    private Boolean isOpponentWithdrawn;
    List<MessageRes> messageList;
}
