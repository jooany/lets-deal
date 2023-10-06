package com.jooany.letsdeal.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MessageAllDeleteReq {
    @NotNull(message = "NULL일 수 없습니다.")
    private Long messageGroupId;

    @Size(min = 1, message = "메시지가 없습니다.")
    @NotNull(message = "NULL일 수 없습니다.")
    private List<Long> messageIds;

    @NotNull(message = "NULL일 수 없습니다.")
    private Long opponentId;

    private Long userId;

    public void setUserId(Long userId){
        this.userId = userId;
    }
}
