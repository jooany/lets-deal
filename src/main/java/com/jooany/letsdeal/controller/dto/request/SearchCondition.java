package com.jooany.letsdeal.controller.dto.request;

import com.jooany.letsdeal.model.enumeration.SaleStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCondition {
    private SaleStatus saleStatus;
    private String keyword;
    private Long categoryId;
    private String targetedUserName;
    private String currentUserName;
    private Boolean isCurrentUserSale;
    private Boolean isCurrentUserOfferedProposal;

}
