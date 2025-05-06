package com.jooany.letsdeal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jooany.letsdeal.model.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findAllByDeletedAtIsNotNullOrderBySortOrderAsc();
}