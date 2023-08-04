package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
    Spring Data JPA 에서 메소드를 찾는 순서
    1. 기본 제공하는 메소드
    2. 메소드 이름에서 특정 규칙을 따라 생성하는 쿼리인 Method Name Query
    3. @Query 어노테이션으로 사용자 정의된 쿼리 메소드
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>, SaleCustomRepository{
    @Transactional
    @Modifying
    @Query(value = "UPDATE sales SET deleted_at = now() WHERE id = :saleId", nativeQuery = true)
    void softDeleteById(@Param("saleId") Long saleId);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.id = :saleId")
    long countSaleById(@Param("saleId") Long saleId);

}