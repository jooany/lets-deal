package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, SaleCustomRepository{

}