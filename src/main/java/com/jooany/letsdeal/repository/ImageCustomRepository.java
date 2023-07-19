package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.model.entity.Image;

import java.util.List;

public interface ImageCustomRepository{
    List<Image> findAllBySaleIdAndOrderBySortOrderAsc(Long saleId);

}