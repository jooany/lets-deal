package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.controller.dto.response.SaleInfoRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SaleCustomRepository {
    Page<SaleRes> findAllBySearchCondition(SearchCondition sale, Pageable pageable);
    Optional<SaleInfoRes> findSaleInfoResById(Long id);

}
