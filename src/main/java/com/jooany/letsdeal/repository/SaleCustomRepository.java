package com.jooany.letsdeal.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.SaleInfoRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;

public interface SaleCustomRepository {
	Page<SaleRes> findAllBySearchCondition(SearchCondition sale, Pageable pageable);

	Optional<SaleInfoRes> findSaleInfoResById(Long id);

}
