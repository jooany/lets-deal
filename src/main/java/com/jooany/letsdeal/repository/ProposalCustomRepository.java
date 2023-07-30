package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProposalCustomRepository{
    Page<ProposalRes> findAllBySaleId(Long saleId, String userName, Pageable pageable);
    List<MyProposalRes> findAllBySaleIdAndUserName(Long saleId, String userName);
}