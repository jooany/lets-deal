package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.SaleListRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SaleCustomRepository {
    Page<SaleListRes> findAllBySearchCondition(SearchCondition sale, Pageable pageable);
    Optional<SaleRes> findSaleResById(Long id);

}
