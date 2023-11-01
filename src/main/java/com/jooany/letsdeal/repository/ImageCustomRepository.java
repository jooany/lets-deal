package com.jooany.letsdeal.repository;

import java.util.List;

import com.jooany.letsdeal.model.entity.Image;

public interface ImageCustomRepository {
	List<Image> findAllBySaleIdAndOrderBySortOrderAsc(Long saleId);

}