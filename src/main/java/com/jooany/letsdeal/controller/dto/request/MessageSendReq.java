package com.jooany.letsdeal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageSendReq {
    @NotNull(message = "NULL일 수 없습니다.")
    private Long sellerId;

    @NotBlank(message = "비어있을 수 없습니다.")
    @Size(max = 100, message = "100자 이하여야 합니다.")
    private String messageContent;
    private Long messageGroupId;

    @NotNull(message = "NULL일 수 없습니다.")
    private Long opponentId;

    private Long userId;

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setMessageGroupId(Long messageGroupId){
        this.messageGroupId = messageGroupId;
    }
}
