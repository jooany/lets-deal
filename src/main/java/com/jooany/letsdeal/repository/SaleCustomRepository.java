package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.SaleListRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleCustomRepository {
    Page<SaleListRes> findAllBySearchCondition(SearchCondition sale, Pageable pageable);

}
