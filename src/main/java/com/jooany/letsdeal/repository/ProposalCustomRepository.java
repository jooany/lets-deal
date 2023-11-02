package com.jooany.letsdeal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;

public interface ProposalCustomRepository {
	Page<ProposalRes> findAllBySaleId(Long saleId, String userName, Pageable pageable);

	List<MyProposalRes> findAllBySaleIdAndUserName(Long saleId, String userName);
}