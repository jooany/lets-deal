package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProposalCustomRepository{
    Page<ProposalRes> findAllBySaleId(Long saleId, String userName, Pageable pageable);
}