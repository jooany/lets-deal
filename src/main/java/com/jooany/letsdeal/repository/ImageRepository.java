package com.jooany.letsdeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jooany.letsdeal.model.entity.Image;
import com.jooany.letsdeal.model.entity.Sale;

import jakarta.transaction.Transactional;

/*
 * @Modifying : 데이터베이스의 데이터 변경 작업이 있을 때 필요한 어노테이션
 * @Query : 사용자 쿼리 정의
 * @Param : 쿼리에 사용할 파라미터를 지정
 * */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, ImageCustomRepository {

	@Transactional
	@Modifying
	@Query("DELETE FROM Image entity WHERE entity.sale = :sale")
	void deleteAllBySale(@Param("sale") Sale sale);
}