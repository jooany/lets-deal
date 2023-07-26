package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.model.entity.Sale;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
/*

 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>, SaleCustomRepository{
    @Transactional
    @Modifying
    @Query("DELETE FROM Sale entity WHERE entity.id = :saleId")
    void deleteById(@Param("saleId") Long saleId);

}