package com.jooany.letsdeal.controller.dto.request;

import com.jooany.letsdeal.model.enumeration.SaleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageListReq {
    private Long saleId;
    private String title;
    private String thumbnailImageUrl;
    private SaleStatus saleStatus;
    private Boolean wasSaleDeleted;
    private Long opponentId;
    private String opponentName;
}
