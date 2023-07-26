package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.model.entity.Image;
import com.jooany.letsdeal.model.entity.Sale;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
/*
* @Modifying : 데이터베이스의 데이터 변경 작업이 있을 때 필요한 어노테이션
* @Query : 사용자 쿼리 정의
* @Param : 쿼리에 사용할 파라미터를 지정
* */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, ImageCustomRepository{

    // Method Name Query를 사용하여 삭제하면 select문 1개, delete문이 N개 즉 N+1 문제가 나타날 수 있다.
    // 이를 방지하기 위해, 하나의 delete문을 실행할 수 있는 쿼리를 직접 정의하였다.
    @Transactional
    @Modifying
    @Query("DELETE FROM Image entity WHERE entity.sale = :sale")
    void deleteAllBySale(@Param("sale") Sale sale);
}